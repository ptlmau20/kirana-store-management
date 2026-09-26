package com.kirana.store.service;

import com.kirana.store.dto.PurchaseDto;
import com.kirana.store.entity.*;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.ProductRepository;
import com.kirana.store.repository.PurchaseRepository;
import com.kirana.store.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public PurchaseService(PurchaseRepository purchaseRepository, SupplierRepository supplierRepository, ProductRepository productRepository, InventoryService inventoryService) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public List<PurchaseDto.Response> getAllPurchases() {
        return purchaseRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PurchaseDto.Response getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase invoice not found with id: " + id));
        return mapToResponse(purchase);
    }

    @Transactional
    public PurchaseDto.Response createPurchase(PurchaseDto.Request request, String performerName) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Purchase invoice must contain at least one item");
        }

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalGst = BigDecimal.ZERO;

        List<PurchaseItem> purchaseItems = new ArrayList<>();

        Purchase purchase = Purchase.builder()
                .invoiceNumber(request.getInvoiceNumber().trim())
                .supplier(supplier)
                .notes(request.getNotes())
                .createdBy(performerName)
                .status("RECEIVED")
                .items(new ArrayList<>())
                .build();

        for (PurchaseDto.ItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            int qty = itemReq.getQuantity();
            BigDecimal unitCost = itemReq.getUnitCost();
            BigDecimal linePreTax = unitCost.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal gstRate = itemReq.getGstRate() != null ? itemReq.getGstRate() : product.getGstRate();
            BigDecimal lineGst = linePreTax.multiply(gstRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = linePreTax.add(lineGst);

            subtotal = subtotal.add(linePreTax);
            totalGst = totalGst.add(lineGst);

            PurchaseItem item = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .unitCost(unitCost)
                    .quantity(qty)
                    .gstRate(gstRate)
                    .totalAmount(lineTotal)
                    .build();

            purchaseItems.add(item);

            product.setCostPrice(unitCost);
            inventoryService.recordMovement(
                    product,
                    StockMovement.MovementType.PURCHASE,
                    qty,
                    request.getInvoiceNumber().trim(),
                    "Supplier Stock Intake",
                    performerName
            );
        }

        BigDecimal netAmount = subtotal.add(totalGst).setScale(2, RoundingMode.HALF_UP);
        BigDecimal paidAmount = request.getPaidAmount() != null ? request.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal dueAmount = netAmount.subtract(paidAmount).setScale(2, RoundingMode.HALF_UP);

        if (dueAmount.compareTo(BigDecimal.ZERO) < 0) {
            dueAmount = BigDecimal.ZERO;
        }

        if (dueAmount.compareTo(BigDecimal.ZERO) > 0) {
            supplier.setBalanceAmount(supplier.getBalanceAmount().add(dueAmount));
            supplierRepository.save(supplier);
        }

        purchase.setTotalAmount(subtotal);
        purchase.setGstAmount(totalGst);
        purchase.setNetAmount(netAmount);
        purchase.setPaidAmount(paidAmount);
        purchase.setDueAmount(dueAmount);
        purchase.getItems().addAll(purchaseItems);

        Purchase saved = purchaseRepository.save(purchase);
        return mapToResponse(saved);
    }

    public PurchaseDto.Response mapToResponse(Purchase p) {
        List<PurchaseDto.ItemResponse> itemResponses = p.getItems().stream()
                .map(i -> PurchaseDto.ItemResponse.builder()
                        .id(i.getId())
                        .productId(i.getProduct().getId())
                        .productName(i.getProduct().getName())
                        .unitCost(i.getUnitCost())
                        .quantity(i.getQuantity())
                        .gstRate(i.getGstRate())
                        .totalAmount(i.getTotalAmount())
                        .build())
                .collect(Collectors.toList());

        return PurchaseDto.Response.builder()
                .id(p.getId())
                .invoiceNumber(p.getInvoiceNumber())
                .supplierId(p.getSupplier().getId())
                .supplierName(p.getSupplier().getName())
                .totalAmount(p.getTotalAmount())
                .gstAmount(p.getGstAmount())
                .netAmount(p.getNetAmount())
                .paidAmount(p.getPaidAmount())
                .dueAmount(p.getDueAmount())
                .status(p.getStatus())
                .notes(p.getNotes())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .items(itemResponses)
                .build();
    }
}
