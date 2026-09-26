package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_payments")
public class CustomerPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "amount_paid", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "reference_note", length = 255)
    private String referenceNote;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public CustomerPayment() {}

    public CustomerPayment(Long id, Customer customer, BigDecimal amountPaid, String paymentMethod, String referenceNote, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.referenceNote = referenceNote;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static CustomerPaymentBuilder builder() { return new CustomerPaymentBuilder(); }

    public static class CustomerPaymentBuilder {
        private Long id;
        private Customer customer;
        private BigDecimal amountPaid;
        private String paymentMethod;
        private String referenceNote;
        private String createdBy;
        private LocalDateTime createdAt;

        public CustomerPaymentBuilder id(Long id) { this.id = id; return this; }
        public CustomerPaymentBuilder customer(Customer customer) { this.customer = customer; return this; }
        public CustomerPaymentBuilder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }
        public CustomerPaymentBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public CustomerPaymentBuilder referenceNote(String referenceNote) { this.referenceNote = referenceNote; return this; }
        public CustomerPaymentBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public CustomerPaymentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public CustomerPayment build() {
            return new CustomerPayment(id, customer, amountPaid, paymentMethod, referenceNote, createdBy, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

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
