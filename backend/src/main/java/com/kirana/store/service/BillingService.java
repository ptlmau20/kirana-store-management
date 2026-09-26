package com.kirana.store.service;

import com.kirana.store.dto.SaleDto;
import com.kirana.store.entity.*;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.InsufficientStockException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.CustomerRepository;
import com.kirana.store.repository.ProductRepository;
import com.kirana.store.repository.SaleReturnRepository;
import com.kirana.store.repository.SaleRepository;
import com.kirana.store.repository.UdharAdjustmentRepository;
import com.kirana.store.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final InventoryService inventoryService;
    private final SaleReturnRepository saleReturnRepository;
    private final UdharAdjustmentRepository udharAdjustmentRepository;
    private final UserRepository userRepository;

    public BillingService(SaleRepository saleRepository, ProductRepository productRepository, CustomerRepository customerRepository,
                          InventoryService inventoryService, SaleReturnRepository saleReturnRepository,
                          UdharAdjustmentRepository udharAdjustmentRepository, UserRepository userRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.inventoryService = inventoryService;
        this.saleReturnRepository = saleReturnRepository;
        this.udharAdjustmentRepository = udharAdjustmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SaleDto.Response> getAllSales() {
        return saleRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<SaleDto.Response> getSales(Pageable pageable) {
        return saleRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public SaleDto.Response getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale/Bill not found with id: " + id));
        return mapToResponse(sale);
    }

    @Transactional(readOnly = true)
    public SaleDto.Response getSaleByBillNumber(String billNumber) {
        Sale sale = saleRepository.findByBillNumber(billNumber.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with bill number: " + billNumber));
        return mapToResponse(sale);
    }

    @Transactional
    public void deleteSale(Long id, String administratorUsername) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale/Bill not found with id: " + id));

        if (saleReturnRepository.existsBySaleId(id)) {
            throw new BadRequestException("A bill with returns cannot be permanently deleted");
        }

        Customer customer = sale.getCustomer();
        if (customer != null) {
            BigDecimal outstandingUdhar = sale.getDueAmount();
            if (customer.getCreditBalance().compareTo(outstandingUdhar) < 0) {
                throw new BadRequestException("This bill's Udhar has already been repaid or adjusted and cannot be safely deleted");
            }

            BigDecimal remainingPurchases = customer.getTotalPurchases().subtract(sale.getNetAmount());
            if (remainingPurchases.compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("Customer purchase totals do not allow this bill to be safely deleted");
            }

            BigDecimal remainingUdhar = customer.getCreditBalance().subtract(outstandingUdhar);
            customer.setCreditBalance(remainingUdhar);
            customer.setTotalPurchases(remainingPurchases);
            customerRepository.save(customer);

            if (outstandingUdhar.compareTo(BigDecimal.ZERO) > 0) {
                udharAdjustmentRepository.save(new UdharAdjustment(
                        customer,
                        outstandingUdhar.negate(),
                        remainingUdhar,
                        "Outstanding Udhar reversed for deleted bill " + sale.getBillNumber(),
                        administratorUsername));
            }
        }

        for (SaleItem item : sale.getItems()) {
            inventoryService.recordMovement(
                    item.getProduct(),
                    StockMovement.MovementType.ADJUSTMENT,
                    item.getQuantity(),
                    sale.getBillNumber(),
                    "Stock restored for permanently deleted bill " + sale.getBillNumber(),
                    administratorUsername);
        }

        saleRepository.delete(sale);
    }

    @Transactional
    public SaleDto.Response createSale(SaleDto.Request request, String cashierUsername) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Cannot create an empty bill. At least one item is required.");
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        } else if (hasCustomerDetails(request)) {
            String name = request.getCustomerName() != null ? request.getCustomerName().trim() : "";
            String phone = request.getCustomerPhone() != null ? request.getCustomerPhone().trim() : "";
            if (name.isEmpty() || !phone.matches("[0-9]{10}")) {
                throw new BadRequestException("Enter a customer name and a valid 10-digit phone number");
            }
            customer = customerRepository.findByPhone(phone)
                    .orElseGet(() -> customerRepository.save(Customer.builder()
                            .name(name)
                            .phone(phone)
                            .build()));
        }

        String billNumber = generateBillNumber();

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        List<SaleItem> saleItems = new ArrayList<>();

        Sale sale = Sale.builder()
                .billNumber(billNumber)
                .customer(customer)
                .paymentMethod(request.getPaymentMethod())
                .cashierName(resolveCashierName(cashierUsername))
                .status("COMPLETED")
                .items(new ArrayList<>())
                .build();

        for (SaleDto.ItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            if (!product.getActive()) {
                throw new BadRequestException("Product '" + product.getName() + "' is inactive and cannot be billed");
            }

            int qty = itemReq.getQuantity();
            if (qty <= 0) {
                throw new BadRequestException("Item quantity must be greater than zero for product: " + product.getName());
            }

            if (product.getStockQuantity() < qty) {
                throw new InsufficientStockException("Insufficient stock for product '" + product.getName() + "'. Requested: " + qty + ", Available: " + product.getStockQuantity());
            }

            BigDecimal unitPrice = itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : product.getSellingPrice();
            BigDecimal linePreTax = unitPrice.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal gstRate = product.getGstRate() != null ? product.getGstRate() : BigDecimal.ZERO;
            BigDecimal lineGst = linePreTax.multiply(gstRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = linePreTax.add(lineGst);

            subtotal = subtotal.add(linePreTax);
            totalGst = totalGst.add(lineGst);

            SaleItem saleItem = SaleItem.builder()
                    .sale(sale)
                    .product(product)
                    .unitPrice(unitPrice)
                    .mrp(product.getMrp())
                    .quantity(qty)
                    .gstRate(gstRate)
                    .itemTotal(lineTotal)
                    .build();

            saleItems.add(saleItem);

            inventoryService.recordMovement(
                    product,
                    StockMovement.MovementType.SALE,
                    -qty,
                    billNumber,
                    "POS Sale Checkout",
                    cashierUsername
            );
        }

        BigDecimal discount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        if (discount.compareTo(subtotal) > 0) {
            throw new BadRequestException("Discount amount cannot exceed bill subtotal");
        }

        BigDecimal netAmount = subtotal.subtract(discount).add(totalGst).setScale(2, RoundingMode.HALF_UP);
        BigDecimal paidAmount = request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO;

        BigDecimal dueAmount = netAmount.subtract(paidAmount).setScale(2, RoundingMode.HALF_UP);
        if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {
            dueAmount = BigDecimal.ZERO;
        }

        if (request.getPaymentMethod() == Sale.PaymentMethod.CREDIT || dueAmount.compareTo(BigDecimal.ZERO) > 0) {
            if (customer == null) {
                throw new BadRequestException("A customer record is mandatory for Credit (Udhar) sales or partial due payments");
            }
            customer.setCreditBalance(customer.getCreditBalance().add(dueAmount));
        }

        if (customer != null) {
            customer.setTotalPurchases(customer.getTotalPurchases().add(netAmount));
            customerRepository.save(customer);
        }

        sale.setTotalAmount(subtotal);
        sale.setDiscountAmount(discount);
        sale.setGstAmount(totalGst);
        sale.setNetAmount(netAmount);
        sale.setPaidAmount(paidAmount);
        sale.setDueAmount(dueAmount);
        sale.getItems().addAll(saleItems);

        Sale savedSale = saleRepository.save(sale);
        return mapToResponse(savedSale);
    }

    private boolean hasCustomerDetails(SaleDto.Request request) {
        return (request.getCustomerName() != null && !request.getCustomerName().isBlank()) ||
                (request.getCustomerPhone() != null && !request.getCustomerPhone().isBlank());
    }

    public String resolveCashierName(String usernameOrName) {
        if (usernameOrName == null || usernameOrName.isBlank()) {
            return usernameOrName;
        }
        return userRepository.findByUsername(usernameOrName)
                .map(User::getFullName)
                .filter(fullName -> fullName != null && !fullName.isBlank())
                .orElse(usernameOrName);
    }

    private synchronized String generateBillNumber() {
        return "INV-" + System.currentTimeMillis() + "-" + (100 + new Random().nextInt(900));
    }

    public SaleDto.Response mapToResponse(Sale sale) {
        List<SaleDto.ItemResponse> itemResponses = sale.getItems().stream()
                .map(i -> SaleDto.ItemResponse.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .hsnCode(i.getProduct().getHsnCode())
                        .unit(i.getProduct().getUnit())
                        .unitPrice(i.getUnitPrice())
                        .mrp(i.getMrp())
                        .quantity(i.getQuantity())
                        .gstRate(i.getGstRate())
                        .itemTotal(i.getItemTotal())
                        .build())
                .collect(Collectors.toList());

        return SaleDto.Response.builder()
                .id(sale.getId())
                .billNumber(sale.getBillNumber())
                .customerId(sale.getCustomer() != null ? sale.getCustomer().getId() : null)
                .customerName(sale.getCustomer() != null ? sale.getCustomer().getName() : "Walk-in Customer")
                .customerPhone(sale.getCustomer() != null ? sale.getCustomer().getPhone() : "")
                .totalAmount(sale.getTotalAmount())
                .discountAmount(sale.getDiscountAmount())
                .gstAmount(sale.getGstAmount())
                .netAmount(sale.getNetAmount())
                .paidAmount(sale.getPaidAmount())
                .dueAmount(sale.getDueAmount())
                .paymentMethod(sale.getPaymentMethod())
                .status(sale.getStatus())
                .cashierName(resolveCashierName(sale.getCashierName()))
                .createdAt(sale.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}
