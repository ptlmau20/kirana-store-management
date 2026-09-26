package com.kirana.store.dto;

import com.kirana.store.entity.Sale.PaymentMethod;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SaleDto {

    public static class Request {
        private Long customerId;
        private String customerName;
        private String customerPhone;

        @NotEmpty(message = "Sale must contain at least one item")
        private List<ItemRequest> items;

        @PositiveOrZero(message = "Discount cannot be negative")
        private BigDecimal discountAmount;

        @NotNull(message = "Payment method is required")
        private PaymentMethod paymentMethod;

        @NotNull(message = "Paid amount is required")
        @PositiveOrZero(message = "Paid amount cannot be negative")
        private BigDecimal paidAmount;

        public Request() {}

        public Request(Long customerId, List<ItemRequest> items, BigDecimal discountAmount, PaymentMethod paymentMethod, BigDecimal paidAmount) {
            this.customerId = customerId;
            this.items = items;
            this.discountAmount = discountAmount;
            this.paymentMethod = paymentMethod;
            this.paidAmount = paidAmount;
        }

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
        public List<ItemRequest> getItems() { return items; }
        public void setItems(List<ItemRequest> items) { this.items = items; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
        public PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
    }

    public static class ItemRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than zero")
        private Integer quantity;

        @Positive(message = "Unit price must be positive")
        private BigDecimal unitPrice;

        public ItemRequest() {}

        public ItemRequest(Long productId, Integer quantity, BigDecimal unitPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }

    public static class Response {
        private Long id;
        private String billNumber;
        private Long customerId;
        private String customerName;
        private String customerPhone;
        private BigDecimal totalAmount;
        private BigDecimal discountAmount;
        private BigDecimal gstAmount;
        private BigDecimal netAmount;
        private BigDecimal paidAmount;
        private BigDecimal dueAmount;
        private PaymentMethod paymentMethod;
        private String status;
        private String cashierName;
        private LocalDateTime createdAt;
        private List<ItemResponse> items;

        public Response() {}

        public Response(Long id, String billNumber, Long customerId, String customerName, String customerPhone, BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal gstAmount, BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount, PaymentMethod paymentMethod, String status, String cashierName, LocalDateTime createdAt, List<ItemResponse> items) {
            this.id = id;
            this.billNumber = billNumber;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.totalAmount = totalAmount;
            this.discountAmount = discountAmount;
            this.gstAmount = gstAmount;
            this.netAmount = netAmount;
            this.paidAmount = paidAmount;
            this.dueAmount = dueAmount;
            this.paymentMethod = paymentMethod;
            this.status = status;
            this.cashierName = cashierName;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private Long id;
            private String billNumber;
            private Long customerId;
            private String customerName;
            private String customerPhone;
            private BigDecimal totalAmount;
            private BigDecimal discountAmount;
            private BigDecimal gstAmount;
            private BigDecimal netAmount;
            private BigDecimal paidAmount;
            private BigDecimal dueAmount;
            private PaymentMethod paymentMethod;
            private String status;
            private String cashierName;
            private LocalDateTime createdAt;
            private List<ItemResponse> items;

            public ResponseBuilder id(Long id) { this.id = id; return this; }
            public ResponseBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
            public ResponseBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
            public ResponseBuilder customerName(String customerName) { this.customerName = customerName; return this; }
            public ResponseBuilder customerPhone(String customerPhone) { this.customerPhone = customerPhone; return this; }
            public ResponseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
            public ResponseBuilder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
            public ResponseBuilder gstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; return this; }
            public ResponseBuilder netAmount(BigDecimal netAmount) { this.netAmount = netAmount; return this; }
            public ResponseBuilder paidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
            public ResponseBuilder dueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; return this; }
            public ResponseBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
            public ResponseBuilder status(String status) { this.status = status; return this; }
            public ResponseBuilder cashierName(String cashierName) { this.cashierName = cashierName; return this; }
            public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ResponseBuilder items(List<ItemResponse> items) { this.items = items; return this; }

            public Response build() {
                return new Response(id, billNumber, customerId, customerName, customerPhone, totalAmount, discountAmount, gstAmount, netAmount, paidAmount, dueAmount, paymentMethod, status, cashierName, createdAt, items);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getBillNumber() { return billNumber; }
        public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public BigDecimal getDiscountAmount() { return discountAmount; }
        public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
        public BigDecimal getGstAmount() { return gstAmount; }
        public void setGstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; }
        public BigDecimal getNetAmount() { return netAmount; }
        public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
        public BigDecimal getDueAmount() { return dueAmount; }
        public void setDueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; }
        public PaymentMethod getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getCashierName() { return cashierName; }
        public void setCashierName(String cashierName) { this.cashierName = cashierName; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
        public List<ItemResponse> getItems() { return items; }
        public void setItems(List<ItemResponse> items) { this.items = items; }
    }

    public static class ItemResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String hsnCode;
        private String unit;
        private BigDecimal unitPrice;
        private BigDecimal mrp;
        private Integer quantity;
        private BigDecimal gstRate;
        private BigDecimal itemTotal;

        public ItemResponse() {}

        public ItemResponse(Long id, Long productId, String productName, String hsnCode, String unit, BigDecimal unitPrice, BigDecimal mrp, Integer quantity, BigDecimal gstRate, BigDecimal itemTotal) {
            this.id = id;
            this.productId = productId;
            this.productName = productName;
            this.hsnCode = hsnCode;
            this.unit = unit;
            this.unitPrice = unitPrice;
            this.mrp = mrp;
            this.quantity = quantity;
            this.gstRate = gstRate;
            this.itemTotal = itemTotal;
        }

        public static ItemResponseBuilder builder() { return new ItemResponseBuilder(); }

        public static class ItemResponseBuilder {
            private Long id;
            private Long productId;
            private String productName;
            private String hsnCode;
            private String unit;
            private BigDecimal unitPrice;
            private BigDecimal mrp;
            private Integer quantity;
            private BigDecimal gstRate;
            private BigDecimal itemTotal;

            public ItemResponseBuilder id(Long id) { this.id = id; return this; }
            public ItemResponseBuilder productId(Long productId) { this.productId = productId; return this; }
            public ItemResponseBuilder productName(String productName) { this.productName = productName; return this; }
            public ItemResponseBuilder hsnCode(String hsnCode) { this.hsnCode = hsnCode; return this; }
            public ItemResponseBuilder unit(String unit) { this.unit = unit; return this; }
            public ItemResponseBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
            public ItemResponseBuilder mrp(BigDecimal mrp) { this.mrp = mrp; return this; }
            public ItemResponseBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
            public ItemResponseBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
            public ItemResponseBuilder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }

            public ItemResponse build() {
                return new ItemResponse(id, productId, productName, hsnCode, unit, unitPrice, mrp, quantity, gstRate, itemTotal);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getHsnCode() { return hsnCode; }
        public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getMrp() { return mrp; }
        public void setMrp(BigDecimal mrp) { this.mrp = mrp; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public BigDecimal getGstRate() { return gstRate; }
        public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }
        public BigDecimal getItemTotal() { return itemTotal; }
        public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
    }
}
