package com.kirana.store.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerDto {

    public static class Request {
        @NotBlank(message = "Customer name is required")
        private String name;

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit number")
        private String phone;

        @Email(message = "Invalid email format")
        private String email;

        private String address;

        public Request() {}

        public Request(String name, String phone, String email, String address) {
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String address;
        private BigDecimal totalPurchases;
        private BigDecimal creditBalance;
        private LocalDateTime createdAt;

        public Response() {}

        public Response(Long id, String name, String phone, String email, String address, BigDecimal totalPurchases, BigDecimal creditBalance, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.totalPurchases = totalPurchases;
            this.creditBalance = creditBalance;
            this.createdAt = createdAt;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private Long id;
            private String name;
            private String phone;
            private String email;
            private String address;
            private BigDecimal totalPurchases;
            private BigDecimal creditBalance;
            private LocalDateTime createdAt;

            public ResponseBuilder id(Long id) { this.id = id; return this; }
            public ResponseBuilder name(String name) { this.name = name; return this; }
            public ResponseBuilder phone(String phone) { this.phone = phone; return this; }
            public ResponseBuilder email(String email) { this.email = email; return this; }
            public ResponseBuilder address(String address) { this.address = address; return this; }
            public ResponseBuilder totalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; return this; }
            public ResponseBuilder creditBalance(BigDecimal creditBalance) { this.creditBalance = creditBalance; return this; }
            public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public Response build() {
                return new Response(id, name, phone, email, address, totalPurchases, creditBalance, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public BigDecimal getTotalPurchases() { return totalPurchases; }
        public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }
        public BigDecimal getCreditBalance() { return creditBalance; }
        public void setCreditBalance(BigDecimal creditBalance) { this.creditBalance = creditBalance; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class PaymentRequest {
        @NotNull(message = "Customer ID is required")
        private Long customerId;

        @NotNull(message = "Payment amount is required")
        @Positive(message = "Payment amount must be greater than zero")
        private BigDecimal amountPaid;

        @NotBlank(message = "Payment method is required")
        private String paymentMethod;

        private String referenceNote;

        public PaymentRequest() {}

        public PaymentRequest(Long customerId, BigDecimal amountPaid, String paymentMethod, String referenceNote) {
            this.customerId = customerId;
            this.amountPaid = amountPaid;
            this.paymentMethod = paymentMethod;
            this.referenceNote = referenceNote;
        }

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public BigDecimal getAmountPaid() { return amountPaid; }
        public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getReferenceNote() { return referenceNote; }
        public void setReferenceNote(String referenceNote) { this.referenceNote = referenceNote; }
    }

    public static class UdharAdjustmentRequest {
        @NotNull(message = "Udhar adjustment amount is required")
        private BigDecimal balanceDelta;

        @NotBlank(message = "Adjustment reason is required")
        private String reason;

        public UdharAdjustmentRequest() {}

        public BigDecimal getBalanceDelta() { return balanceDelta; }
        public void setBalanceDelta(BigDecimal balanceDelta) { this.balanceDelta = balanceDelta; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class PaymentResponse {
        private Long id;
        private Long customerId;
        private String customerName;
        private BigDecimal amountPaid;
        public String paymentMethod;
        private String referenceNote;
        private String createdBy;
        private LocalDateTime createdAt;

        public PaymentResponse() {}

        public PaymentResponse(Long id, Long customerId, String customerName, BigDecimal amountPaid, String paymentMethod, String referenceNote, String createdBy, LocalDateTime createdAt) {
            this.id = id;
            this.customerId = customerId;
            this.customerName = customerName;
            this.amountPaid = amountPaid;
            this.paymentMethod = paymentMethod;
            this.referenceNote = referenceNote;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
        }

        public static PaymentResponseBuilder builder() { return new PaymentResponseBuilder(); }

        public static class PaymentResponseBuilder {
            private Long id;
            private Long customerId;
            private String customerName;
            private BigDecimal amountPaid;
            private String paymentMethod;
            private String referenceNote;
            private String createdBy;
            private LocalDateTime createdAt;

            public PaymentResponseBuilder id(Long id) { this.id = id; return this; }
            public PaymentResponseBuilder customerId(Long customerId) { this.customerId = customerId; return this; }
            public PaymentResponseBuilder customerName(String customerName) { this.customerName = customerName; return this; }
            public PaymentResponseBuilder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }
            public PaymentResponseBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
            public PaymentResponseBuilder referenceNote(String referenceNote) { this.referenceNote = referenceNote; return this; }
            public PaymentResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
            public PaymentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public PaymentResponse build() {
                return new PaymentResponse(id, customerId, customerName, amountPaid, paymentMethod, referenceNote, createdBy, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public BigDecimal getAmountPaid() { return amountPaid; }
        public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getReferenceNote() { return referenceNote; }
        public void setReferenceNote(String referenceNote) { this.referenceNote = referenceNote; }
        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }
}
