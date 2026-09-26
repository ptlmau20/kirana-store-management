package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
public class Sale {

    public enum PaymentMethod {
        CASH,
        UPI,
        CARD,
        CREDIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_number", nullable = false, unique = true, length = 50)
    private String billNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "gst_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "net_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "paid_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "due_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal dueAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    @Column(length = 20)
    private String status;

    @Column(name = "cashier_name", nullable = false, length = 100)
    private String cashierName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();

    public Sale() {}

    public Sale(Long id, String billNumber, Customer customer, BigDecimal totalAmount, BigDecimal discountAmount, BigDecimal gstAmount, BigDecimal netAmount, BigDecimal paidAmount, BigDecimal dueAmount, PaymentMethod paymentMethod, String status, String cashierName, LocalDateTime createdAt, List<SaleItem> items) {
        this.id = id;
        this.billNumber = billNumber;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.gstAmount = gstAmount;
        this.netAmount = netAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.paymentMethod = paymentMethod;
        this.status = status != null ? status : "COMPLETED";
        this.cashierName = cashierName;
        this.createdAt = createdAt;
        this.items = items != null ? items : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = "COMPLETED";
    }

    public static SaleBuilder builder() { return new SaleBuilder(); }

    public static class SaleBuilder {
        private Long id;
        private String billNumber;
        private Customer customer;
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
        private List<SaleItem> items = new ArrayList<>();

        public SaleBuilder id(Long id) { this.id = id; return this; }
        public SaleBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
        public SaleBuilder customer(Customer customer) { this.customer = customer; return this; }
        public SaleBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public SaleBuilder discountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
        public SaleBuilder gstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; return this; }
        public SaleBuilder netAmount(BigDecimal netAmount) { this.netAmount = netAmount; return this; }
        public SaleBuilder paidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
        public SaleBuilder dueAmount(BigDecimal dueAmount) { this.dueAmount = dueAmount; return this; }
        public SaleBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public SaleBuilder status(String status) { this.status = status; return this; }
        public SaleBuilder cashierName(String cashierName) { this.cashierName = cashierName; return this; }
        public SaleBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SaleBuilder items(List<SaleItem> items) { this.items = items; return this; }

        public Sale build() {
            return new Sale(id, billNumber, customer, totalAmount, discountAmount, gstAmount, netAmount, paidAmount, dueAmount, paymentMethod, status, cashierName, createdAt, items);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

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

    public List<SaleItem> getItems() { return items; }
    public void setItems(List<SaleItem> items) { this.items = items; }
}
