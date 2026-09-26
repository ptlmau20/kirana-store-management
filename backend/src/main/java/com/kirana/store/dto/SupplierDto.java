package com.kirana.store.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SupplierDto {

    public static class Request {
        @NotBlank(message = "Supplier name is required")
        private String name;

        private String contactPerson;

        @NotBlank(message = "Phone number is required")
        private String phone;

        @Email(message = "Invalid email format")
        private String email;

        private String gstNumber;
        private String address;

        public Request() {}

        public Request(String name, String contactPerson, String phone, String email, String gstNumber, String address) {
            this.name = name;
            this.contactPerson = contactPerson;
            this.phone = phone;
            this.email = email;
            this.gstNumber = gstNumber;
            this.address = address;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getGstNumber() { return gstNumber; }
        public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class Response {
        private Long id;
        private String name;
        private String contactPerson;
        private String phone;
        private String email;
        private String gstNumber;
        private String address;
        private BigDecimal balanceAmount;
        private LocalDateTime createdAt;

        public Response() {}

        public Response(Long id, String name, String contactPerson, String phone, String email, String gstNumber, String address, BigDecimal balanceAmount, LocalDateTime createdAt) {
            this.id = id;
            this.name = name;
            this.contactPerson = contactPerson;
            this.phone = phone;
            this.email = email;
            this.gstNumber = gstNumber;
            this.address = address;
            this.balanceAmount = balanceAmount;
            this.createdAt = createdAt;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private Long id;
            private String name;
            private String contactPerson;
            private String phone;
            private String email;
            private String gstNumber;
            private String address;
            private BigDecimal balanceAmount;
            private LocalDateTime createdAt;

            public ResponseBuilder id(Long id) { this.id = id; return this; }
            public ResponseBuilder name(String name) { this.name = name; return this; }
            public ResponseBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
            public ResponseBuilder phone(String phone) { this.phone = phone; return this; }
            public ResponseBuilder email(String email) { this.email = email; return this; }
            public ResponseBuilder gstNumber(String gstNumber) { this.gstNumber = gstNumber; return this; }
            public ResponseBuilder address(String address) { this.address = address; return this; }
            public ResponseBuilder balanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; return this; }
            public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public Response build() {
                return new Response(id, name, contactPerson, phone, email, gstNumber, address, balanceAmount, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getGstNumber() { return gstNumber; }
        public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public BigDecimal getBalanceAmount() { return balanceAmount; }
        public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class PaymentRequest {
        @NotNull(message = "Supplier ID is required")
        private Long supplierId;

        @NotNull(message = "Payment amount is required")
        @Positive(message = "Payment amount must be greater than zero")
        private BigDecimal amountPaid;

        @NotBlank(message = "Payment method is required")
        private String paymentMethod;

        private String referenceNote;

        public PaymentRequest() {}

        public PaymentRequest(Long supplierId, BigDecimal amountPaid, String paymentMethod, String referenceNote) {
            this.supplierId = supplierId;
            this.amountPaid = amountPaid;
            this.paymentMethod = paymentMethod;
            this.referenceNote = referenceNote;
        }

        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public BigDecimal getAmountPaid() { return amountPaid; }
        public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getReferenceNote() { return referenceNote; }
        public void setReferenceNote(String referenceNote) { this.referenceNote = referenceNote; }
    }

    public static class PaymentResponse {
        private Long id;
        private Long supplierId;
        private String supplierName;
        private BigDecimal amountPaid;
        private String paymentMethod;
        private String referenceNote;
        private String createdBy;
        private LocalDateTime createdAt;

        public PaymentResponse() {}

        public PaymentResponse(Long id, Long supplierId, String supplierName, BigDecimal amountPaid, String paymentMethod, String referenceNote, String createdBy, LocalDateTime createdAt) {
            this.id = id;
            this.supplierId = supplierId;
            this.supplierName = supplierName;
            this.amountPaid = amountPaid;
            this.paymentMethod = paymentMethod;
            this.referenceNote = referenceNote;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
        }

        public static PaymentResponseBuilder builder() { return new PaymentResponseBuilder(); }

        public static class PaymentResponseBuilder {
            private Long id;
            private Long supplierId;
            private String supplierName;
            private BigDecimal amountPaid;
            private String paymentMethod;
            private String referenceNote;
            private String createdBy;
            private LocalDateTime createdAt;

            public PaymentResponseBuilder id(Long id) { this.id = id; return this; }
            public PaymentResponseBuilder supplierId(Long supplierId) { this.supplierId = supplierId; return this; }
            public PaymentResponseBuilder supplierName(String supplierName) { this.supplierName = supplierName; return this; }
            public PaymentResponseBuilder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }
            public PaymentResponseBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
            public PaymentResponseBuilder referenceNote(String referenceNote) { this.referenceNote = referenceNote; return this; }
            public PaymentResponseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
            public PaymentResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

            public PaymentResponse build() {
                return new PaymentResponse(id, supplierId, supplierName, amountPaid, paymentMethod, referenceNote, createdBy, createdAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
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
