package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_payments")
public class SupplierPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

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

    public SupplierPayment() {}

    public SupplierPayment(Long id, Supplier supplier, BigDecimal amountPaid, String paymentMethod, String referenceNote, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.supplier = supplier;
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

    public static SupplierPaymentBuilder builder() { return new SupplierPaymentBuilder(); }

    public static class SupplierPaymentBuilder {
        private Long id;
        private Supplier supplier;
        private BigDecimal amountPaid;
        private String paymentMethod;
        private String referenceNote;
        private String createdBy;
        private LocalDateTime createdAt;

        public SupplierPaymentBuilder id(Long id) { this.id = id; return this; }
        public SupplierPaymentBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public SupplierPaymentBuilder amountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; return this; }
        public SupplierPaymentBuilder paymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public SupplierPaymentBuilder referenceNote(String referenceNote) { this.referenceNote = referenceNote; return this; }
        public SupplierPaymentBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public SupplierPaymentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public SupplierPayment build() {
            return new SupplierPayment(id, supplier, amountPaid, paymentMethod, referenceNote, createdBy, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

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
