package com.kirana.store.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PurchaseDto {

    public static class Request {
        @NotBlank(message = "Supplier invoice number is required")
        private String invoiceNumber;

        @NotNull(message = "Supplier ID is required")
        private Long supplierId;

        @NotEmpty(message = "Purchase must contain at least one item")
        private List<ItemRequest> items;

        @NotNull(message = "Paid amount is required")
        @PositiveOrZero(message = "Paid amount cannot be negative")
        private BigDecimal paidAmount;

        private String notes;

        public Request() {}

        public Request(String invoiceNumber, Long supplierId, List<ItemRequest> items, BigDecimal paidAmount, String notes) {
            this.invoiceNumber = invoiceNumber;
            this.supplierId = supplierId;
            this.items = items;
            this.paidAmount = paidAmount;
            this.notes = notes;
        }

        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public List<ItemRequest> getItems() { return items; }
        public void setItems(List<ItemRequest> items) { this.items = items; }
        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class ItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Unit cost is required")
        @Positive(message = "Unit cost must be positive")
        private BigDecimal unitCost;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        private Integer quantity;

        @NotNull(message = "GST rate is required")
        @PositiveOrZero(message = "GST rate must be zero or positive")
        private BigDecimal gstRate;

        public ItemRequest() {}

        public ItemRequest(Long productId, BigDecimal unitCost, Integer quantity, BigDecimal gstRate) {
            this.productId = productId;
            this.unitCost = unitCost;
            this.quantity = quantity;
            this.gstRate = gstRate;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getGstRate() { return gstRate; }
        public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
    }

    public static class Response {
        private Long id;
        private String invoiceNumber;
        private Long supplierId;
        private String supplierName;
        private BigDecimal totalAmount;
        private BigDecimal gstAmount;
        private BigDecimal netAmount;
        private BigDecimal paidAmount;
        private BigDecimal dueAmount;
        private String status;
        private String notes;
        private String createdBy;
        private LocalDateTime createdAt;
        private List<ItemResponse> items;

        public Response() {}

        public Response(Long id, String invoiceNumber, Long supplierId, String supplierName, BigDecimal totalAmount, BigDecimal gstAmount, BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount, String status, String notes, String createdBy, LocalDateTime createdAt, List<ItemResponse> items) {
            this.id = id;
            this.invoiceNumber = invoiceNumber;
            this.supplierId = supplierId;
            this.supplierName = supplierName;
            this.totalAmount = totalAmount;
            this.gstAmount = gstAmount;
            this.netAmount = netAmount;
            this.paidAmount = paidAmount;
            this.dueAmount = dueAmount;
            this.status = status;
            this.notes = notes;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private Long id;
            private String invoiceNumber;
            private Long supplierId;
            private String supplierName;
            private BigDecimal totalAmount;
            private BigDecimal gstAmount;
            private BigDecimal netAmount;
            private BigDecimal paidAmount;
            private BigDecimal dueAmount;
            private String status;
            private String notes;
            private String createdBy;
            private LocalDateTime createdAt;
            private List<ItemResponse> items;

            public ResponseBuilder id(Long id) { this.id = id; return this; }
            public ResponseBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
            public ResponseBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
            public ResponseBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
            public ResponseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
            public ResponseBuilder gstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; return this; }
            public ResponseBuilder netAmount(BigDecimal netAmount) { this.netAmount = netAmount; return this; }
            public ResponseBuilder paidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
            public ResponseBuilder dueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; return this; }
            public ResponseBuilder status(String status) { this.status = status; return this; }
            public ResponseBuilder notes(String notes) { this.notes = notes; return this; }
            public ResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
            public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ResponseBuilder items(List<ItemResponse> items) { this.items = items; return this; }

            public Response build() {
                return new Response(id, invoiceNumber, supplierId, supplierName, totalAmount, gstAmount, netAmount, paidAmount, dueAmount, status, notes, createdBy, createdAt, items);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public BigDecimal getGstAmount() { return gstAmount; }
        public void setGstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; }
        public BigDecimal getNetAmount() { return netAmount; }
        public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
        public BigDecimal getDueAmount() { return dueAmount; }
        public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public List<ItemResponse> getItems() { return items; }
        public void setItems(List<ItemResponse> items) { this.items = items; }
    }

    public static class ItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal unitCost;
        private Integer quantity;
        private BigDecimal gstRate;
        private BigDecimal totalAmount;

        public ItemResponse() {}

        public ItemResponse(Long id, Long productId, String productName, BigDecimal unitCost, Integer quantity, BigDecimal gstRate, BigDecimal totalAmount) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.unitCost = unitCost;
            this.quantity = quantity;
            this.gstRate = gstRate;
            this.totalAmount = totalAmount;
        }

        public static ItemResponseBuilder builder() { return new ItemResponseBuilder(); }

        public static class ItemResponseBuilder {
            private Long id;
            private Long productId;
            private String productName;
            private BigDecimal unitCost;
            private Integer quantity;
            private BigDecimal gstRate;
            private BigDecimal totalAmount;

            public ItemResponseBuilder id(Long id) { this.id = id; return this; }
            public ItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public ItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
            public ItemResponseBuilder unitCost(BigDecimal unitCost) { this.unitCost = unitCost; return this; }
            public ItemResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public ItemResponseBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
            public ItemResponseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }

            public ItemResponse build() {
                return new ItemResponse(id, productId, productName, unitCost, quantity, gstRate, totalAmount);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public BigDecimal getUnitCost() { return unitCost; }
        public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getGstRate() { return gstRate; }
        public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }
}
