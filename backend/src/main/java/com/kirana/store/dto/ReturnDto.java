package com.kirana.store.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ReturnDto {

    public static class SaleReturnRequest {
        @NotNull(message = "Sale ID is required")
        private Long saleId;

        @NotEmpty(message = "Return must contain at least one item")
        private List<SaleReturnItemRequest> items;

        private String returnReason;

        public SaleReturnRequest() {}

        public SaleReturnRequest(Long saleId, List<SaleReturnItemRequest> items, String returnReason) {
            this.saleId = saleId;
            this.items = items;
            this.returnReason = returnReason;
        }

        public Long getSaleId() { return saleId; }
        public void setSaleId(Long saleId) { this.saleId = saleId; }
        public List<SaleReturnItemRequest> getItems() { return items; }
        public void setItems(List<SaleReturnItemRequest> items) { this.items = items; }
        public String getReturnReason() { return returnReason; }
        public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
    }

    public static class SaleReturnItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        private Integer quantity;

        public SaleReturnItemRequest() {}

        public SaleReturnItemRequest(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

    public static class SaleReturnResponse {
        private Long id;
        private String returnNumber;
        private Long saleId;
        private String billNumber;
        private BigDecimal refundAmount;
        private String returnReason;
        private String createdBy;
        private LocalDateTime createdAt;
        private List<SaleReturnItemResponse> items;

        public SaleReturnResponse() {}

        public SaleReturnResponse(Long id, String returnNumber, Long saleId, String billNumber, BigDecimal refundAmount, String returnReason, String createdBy, LocalDateTime createdAt, List<SaleReturnItemResponse> items) {
            this.id = id;
            this.returnNumber = returnNumber;
            this.saleId = saleId;
            this.billNumber = billNumber;
            this.refundAmount = refundAmount;
            this.returnReason = returnReason;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static SaleReturnResponseBuilder builder() { return new SaleReturnResponseBuilder(); }

        public static class SaleReturnResponseBuilder {
            private Long id;
            private String returnNumber;
            private Long saleId;
            private String billNumber;
            private BigDecimal refundAmount;
            private String returnReason;
            private String createdBy;
            private LocalDateTime createdAt;
            private List<SaleReturnItemResponse> items;

            public SaleReturnResponseBuilder id(Long id) { this.id = id; return this; }
            public SaleReturnResponseBuilder returnNumber(String returnNumber) { this.returnNumber = returnNumber; return this; }
            public SaleReturnResponseBuilder saleId(Long saleId) { this.saleId = saleId; return this; }
            public SaleReturnResponseBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
            public SaleReturnResponseBuilder refundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; return this; }
            public SaleReturnResponseBuilder returnReason(String returnReason) { this.returnReason = returnReason; return this; }
            public SaleReturnResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
            public SaleReturnResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public SaleReturnResponseBuilder items(List<SaleReturnItemResponse> items) { this.items = items; return this; }

            public SaleReturnResponse build() {
                return new SaleReturnResponse(id, returnNumber, saleId, billNumber, refundAmount, returnReason, createdBy, createdAt, items);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReturnNumber() { return returnNumber; }
        public void setReturnNumber(String returnNumber) { this.returnNumber = returnNumber; }
        public Long getSaleId() { return saleId; }
        public void setSaleId(Long saleId) { this.saleId = saleId; }
        public String getBillNumber() { return billNumber; }
        public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
        public BigDecimal getRefundAmount() { return refundAmount; }
        public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
        public String getReturnReason() { return returnReason; }
        public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public List<SaleReturnItemResponse> getItems() { return items; }
        public void setItems(List<SaleReturnItemResponse> items) { this.items = items; }
    }

    public static class SaleReturnItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal refundUnitPrice;
        private BigDecimal itemTotal;

        public SaleReturnItemResponse() {}

        public SaleReturnItemResponse(Long id, Long productId, String productName, Integer quantity, BigDecimal refundUnitPrice, BigDecimal itemTotal) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.refundUnitPrice = refundUnitPrice;
            this.itemTotal = itemTotal;
        }

        public static SaleReturnItemResponseBuilder builder() { return new SaleReturnItemResponseBuilder(); }

        public static class SaleReturnItemResponseBuilder {
            private Long id;
            private Long productId;
            private String productName;
            private Integer quantity;
            private BigDecimal refundUnitPrice;
            private BigDecimal itemTotal;

            public SaleReturnItemResponseBuilder id(Long id) { this.id = id; return this; }
            public SaleReturnItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public SaleReturnItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
            public SaleReturnItemResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public SaleReturnItemResponseBuilder refundUnitPrice(BigDecimal refundUnitPrice) { this.refundUnitPrice = refundUnitPrice; return this; }
            public SaleReturnItemResponseBuilder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }

            public SaleReturnItemResponse build() {
                return new SaleReturnItemResponse(id, productId, productName, quantity, refundUnitPrice, itemTotal);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getRefundUnitPrice() { return refundUnitPrice; }
        public void setRefundUnitPrice(BigDecimal refundUnitPrice) { this.refundUnitPrice = refundUnitPrice; }
        public BigDecimal getItemTotal() { return itemTotal; }
        public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    }

    public static class PurchaseReturnRequest {
        @NotNull(message = "Purchase ID is required")
        private Long purchaseId;

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;

        private String reason;

        public PurchaseReturnRequest() {}

        public PurchaseReturnRequest(Long purchaseId, Long productId, Integer quantity, String reason) {
            this.purchaseId = purchaseId;
            this.productId = productId;
            this.quantity = quantity;
            this.reason = reason;
        }

        public Long getPurchaseId() { return purchaseId; }
        public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class PurchaseReturnResponse {
        private Long id;
        private String returnNumber;
        private Long purchaseId;
        private String invoiceNumber;
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal returnAmount;
        private String reason;
        private String createdBy;
        private LocalDateTime createdAt;

        public PurchaseReturnResponse() {}

        public PurchaseReturnResponse(Long id, String returnNumber, Long purchaseId, String invoiceNumber, Long productId, String productName, Integer quantity, BigDecimal returnAmount, String reason, String createdBy, LocalDateTime createdAt) {
            this.id = id;
            this.returnNumber = returnNumber;
            this.purchaseId = purchaseId;
            this.invoiceNumber = invoiceNumber;
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.returnAmount = returnAmount;
            this.reason = reason;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
        }

        public static PurchaseReturnResponseBuilder builder() { return new PurchaseReturnResponseBuilder(); }

        public static class PurchaseReturnResponseBuilder {
            private Long id;
            private String returnNumber;
            private Long purchaseId;
            private String invoiceNumber;
            private Long productId;
            private String productName;
            private Integer quantity;
            private BigDecimal returnAmount;
            private String reason;
            private String createdBy;
            private LocalDateTime createdAt;

            public PurchaseReturnResponseBuilder id(Long id) { this.id = id; return this; }
            public PurchaseReturnResponseBuilder returnNumber(String returnNumber) { this.returnNumber = returnNumber; return this; }
            public PurchaseReturnResponseBuilder purchaseId(Long purchaseId) { this.purchaseId = purchaseId; return this; }
            public PurchaseReturnResponseBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
            public PurchaseReturnResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public PurchaseReturnResponseBuilder productName(String productName) { this.productName = productName; return this; }
            public PurchaseReturnResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public PurchaseReturnResponseBuilder returnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; return this; }
            public PurchaseReturnResponseBuilder reason(String reason) { this.reason = reason; return this; }
            public PurchaseReturnResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
            public PurchaseReturnResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public PurchaseReturnResponse build() {
                return new PurchaseReturnResponse(id, returnNumber, purchaseId, invoiceNumber, productId, productName, quantity, returnAmount, reason, createdBy, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getReturnNumber() { return returnNumber; }
        public void setReturnNumber(String returnNumber) { this.returnNumber = returnNumber; }
        public Long getPurchaseId() { return purchaseId; }
        public void setPurchaseId(Long purchaseId) { this.purchaseId = purchaseId; }
        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getReturnAmount() { return returnAmount; }
        public void setReturnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
