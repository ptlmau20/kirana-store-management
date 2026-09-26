package com.kirana.store.service;

import com.kirana.store.dto.ReturnDto;
import com.kirana.store.entity.*;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ReturnService {

    private final SaleRepository saleRepository;
    private final SaleReturnRepository saleReturnRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final InventoryService inventoryService;

    public ReturnService(SaleRepository saleRepository, SaleReturnRepository saleReturnRepository, PurchaseRepository purchaseRepository, PurchaseReturnRepository purchaseReturnRepository, ProductRepository productRepository, CustomerRepository customerRepository, SupplierRepository supplierRepository, InventoryService inventoryService) {
        this.saleRepository = saleRepository;
        this.saleReturnRepository = saleReturnRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public List<ReturnDto.SaleReturnResponse> getAllSaleReturns() {
        return saleReturnRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToSaleReturnResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReturnDto.SaleReturnResponse createSaleReturn(ReturnDto.SaleReturnRequest request, String performerName) {
        Sale sale = saleRepository.findById(request.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + request.getSaleId()));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Return request must contain at least one item");
        }

        String returnNumber = "RET-" + System.currentTimeMillis() + "-" + (100 + new Random().nextInt(900));
        BigDecimal totalRefund = BigDecimal.ZERO;

        List<SaleReturnItem> returnItems = new ArrayList<>();

        SaleReturn saleReturn = SaleReturn.builder()
                .returnNumber(returnNumber)
                .sale(sale)
                .returnReason(request.getReturnReason())
                .createdBy(performerName)
                .items(new ArrayList<>())
                .build();

        for (ReturnDto.SaleReturnItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            SaleItem saleItem = sale.getItems().stream()
                    .filter(si -> si.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("Product '" + product.getName() + "' was not part of original bill " + sale.getBillNumber()));

            int returnQty = itemReq.getQuantity();
            if (returnQty > saleItem.getQuantity()) {
                throw new BadRequestException("Return quantity (" + returnQty + ") exceeds original purchased quantity (" + saleItem.getQuantity() + ") for product: " + product.getName());
            }

            BigDecimal refundUnitPrice = saleItem.getUnitPrice();
            BigDecimal itemTotal = refundUnitPrice.multiply(BigDecimal.valueOf(returnQty)).setScale(2, RoundingMode.HALF_UP);

            totalRefund = totalRefund.add(itemTotal);

            SaleReturnItem returnItem = SaleReturnItem.builder()
                    .saleReturn(saleReturn)
                    .product(product)
                    .quantity(returnQty)
                    .refundUnitPrice(refundUnitPrice)
                    .itemTotal(itemTotal)
                    .build();

            returnItems.add(returnItem);

            inventoryService.recordMovement(
                    product,
                    StockMovement.MovementType.SALES_RETURN,
                    returnQty,
                    returnNumber,
                    "Sales Return for Bill " + sale.getBillNumber(),
                    performerName
            );
        }

        saleReturn.setRefundAmount(totalRefund);
        saleReturn.getItems().addAll(returnItems);

        if (sale.getCustomer() != null && sale.getCustomer().getCreditBalance().compareTo(BigDecimal.ZERO) > 0) {
            Customer customer = sale.getCustomer();
            BigDecimal newCreditBalance = customer.getCreditBalance().subtract(totalRefund);
            if (newCreditBalance.compareTo(BigDecimal.ZERO) < 0) {
                newCreditBalance = BigDecimal.ZERO;
            }
            customer.setCreditBalance(newCreditBalance);
            customerRepository.save(customer);
        }

        sale.setStatus("RETURNED");
        saleRepository.save(sale);

        SaleReturn saved = saleReturnRepository.save(saleReturn);
        return mapToSaleReturnResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReturnDto.PurchaseReturnResponse> getAllPurchaseReturns() {
        return purchaseReturnRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToPurchaseReturnResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReturnDto.PurchaseReturnResponse createPurchaseReturn(ReturnDto.PurchaseReturnRequest request, String performerName) {
        Purchase purchase = purchaseRepository.findById(request.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFoundException("Purchase not found with id: " + request.getPurchaseId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        PurchaseItem purchaseItem = purchase.getItems().stream()
                .filter(pi -> pi.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Product '" + product.getName() + "' was not found in purchase invoice " + purchase.getInvoiceNumber()));

        int returnQty = request.getQuantity();
        if (returnQty > purchaseItem.getQuantity()) {
            throw new BadRequestException("Return quantity exceeds purchased quantity");
        }

        String returnNumber = "PRET-" + System.currentTimeMillis();
        BigDecimal returnAmount = purchaseItem.getUnitCost().multiply(BigDecimal.valueOf(returnQty)).setScale(2, RoundingMode.HALF_UP);

        inventoryService.recordMovement(
                product,
                StockMovement.MovementType.PURCHASE_RETURN,
                -returnQty,
                returnNumber,
                "Purchase Return for Invoice " + purchase.getInvoiceNumber(),
                performerName
        );

        Supplier supplier = purchase.getSupplier();
        BigDecimal newBalance = supplier.getBalanceAmount().subtract(returnAmount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }
        supplier.setBalanceAmount(newBalance);
        supplierRepository.save(supplier);

        PurchaseReturn pReturn = PurchaseReturn.builder()
                .returnNumber(returnNumber)
                .purchase(purchase)
                .product(product)
                .quantity(returnQty)
                .returnAmount(returnAmount)
                .reason(request.getReason())
                .createdBy(performerName)
                .build();

        PurchaseReturn saved = purchaseReturnRepository.save(pReturn);
        return mapToPurchaseReturnResponse(saved);
    }

    private ReturnDto.SaleReturnResponse mapToSaleReturnResponse(SaleReturn sr) {
        List<ReturnDto.SaleReturnItemResponse> itemResponses = sr.getItems().stream()
                .map(i -> ReturnDto.SaleReturnItemResponse.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .refundUnitPrice(i.getRefundUnitPrice())
                        .itemTotal(i.getItemTotal())
                        .build())
                .collect(Collectors.toList());

        return ReturnDto.SaleReturnResponse.builder()
                .id(sr.getId())
                .returnNumber(sr.getReturnNumber())
                .saleId(sr.getSale().getId())
                .billNumber(sr.getSale().getBillNumber())
                .refundAmount(sr.getRefundAmount())
                .returnReason(sr.getReturnReason())
                .createdBy(sr.getCreatedBy())
                .createdAt(sr.getCreatedAt())
                .items(itemResponses)
                .build();
    }

    private ReturnDto.PurchaseReturnResponse mapToPurchaseReturnResponse(PurchaseReturn pr) {
        return ReturnDto.PurchaseReturnResponse.builder()
                .id(pr.getId())
                .returnNumber(pr.getReturnNumber())
                .purchaseId(pr.getPurchase().getId())
                .invoiceNumber(pr.getPurchase().getInvoiceNumber())
                .productId(pr.getProduct().getId())
                .productName(pr.getProduct().getName())
                .quantity(pr.getQuantity())
                .returnAmount(pr.getReturnAmount())
                .reason(pr.getReason())
                .createdBy(pr.getCreatedBy())
                .createdAt(pr.getCreatedAt())
                .build();
    }
}
