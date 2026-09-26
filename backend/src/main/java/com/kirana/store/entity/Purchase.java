package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, length = 50)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "gst_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "net_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "paid_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "due_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal dueAmount;

    @Column(length = 30)
    private String status;

    @Column(length = 255)
    private String notes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();

    public Purchase() {}

    public Purchase(Long id, String invoiceNumber, Supplier supplier, BigDecimal totalAmount, BigDecimal gstAmount, BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount, String status, String notes, String createdBy, LocalDateTime createdAt, List<PurchaseItem> items) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.supplier = supplier;
        this.totalAmount = totalAmount;
        this.gstAmount = gstAmount;
        this.netAmount = netAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.status = status;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.items = items != null ? items : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static PurchaseBuilder builder() { return new PurchaseBuilder(); }

    public static class PurchaseBuilder {
        private Long id;
        private String invoiceNumber;
        private Supplier supplier;
        private BigDecimal totalAmount;
        private BigDecimal gstAmount;
        private BigDecimal netAmount;
        private BigDecimal paidAmount;
        private BigDecimal dueAmount;
        private String status;
        private String notes;
        private String createdBy;
        private LocalDateTime createdAt;
        private List<PurchaseItem> items = new ArrayList<>();

        public PurchaseBuilder id(Long id) { this.id = id; return this; }
        public PurchaseBuilder invoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
        public PurchaseBuilder supplier(Supplier supplier) { this.supplier = supplier; return this; }
        public PurchaseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public PurchaseBuilder gstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; return this; }
        public PurchaseBuilder netAmount(BigDecimal netAmount) { this.netAmount = netAmount; return this; }
        public PurchaseBuilder paidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
        public PurchaseBuilder dueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; return this; }
        public PurchaseBuilder status(String status) { this.status = status; return this; }
        public PurchaseBuilder notes(String notes) { this.notes = notes; return this; }
        public PurchaseBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public PurchaseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PurchaseBuilder items(List<PurchaseItem> items) { this.items = items; return this; }

        public Purchase build() {
            return new Purchase(id, invoiceNumber, supplier, totalAmount, gstAmount, netAmount, paidAmount, dueAmount, status, notes, createdBy, createdAt, items);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public Supplier getSupplier() { return supplier; }
    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

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

    public List<PurchaseItem> getItems() { return items; }
    public void setItems(List<PurchaseItem> items) { this.items = items; }
}
