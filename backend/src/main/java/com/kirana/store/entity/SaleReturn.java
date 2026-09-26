package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sale_returns")
public class SaleReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "return_number", nullable = false, unique = true, length = 50)
    private String returnNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "refund_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal refundAmount;

    @Column(name = "return_reason", length = 255)
    private String returnReason;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleReturnItem> items = new ArrayList<>();

    public SaleReturn() {}

    public SaleReturn(Long id, String returnNumber, Sale sale, BigDecimal refundAmount, String returnReason, String createdBy, LocalDateTime createdAt, List<SaleReturnItem> items) {
        this.id = id;
        this.returnNumber = returnNumber;
        this.sale = sale;
        this.refundAmount = refundAmount;
        this.returnReason = returnReason;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.items = items != null ? items : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static SaleReturnBuilder builder() { return new SaleReturnBuilder(); }

    public static class SaleReturnBuilder {
        private Long id;
        private String returnNumber;
        private Sale sale;
        private BigDecimal refundAmount;
        private String returnReason;
        private String createdBy;
        private LocalDateTime createdAt;
        private List<SaleReturnItem> items = new ArrayList<>();

        public SaleReturnBuilder id(Long id) { this.id = id; return this; }
        public SaleReturnBuilder returnNumber(String returnNumber) { this.returnNumber = returnNumber; return this; }
        public SaleReturnBuilder sale(Sale sale) { this.sale = sale; return this; }
        public SaleReturnBuilder refundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; return this; }
        public SaleReturnBuilder returnReason(String returnReason) { this.returnReason = returnReason; return this; }
        public SaleReturnBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public SaleReturnBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public SaleReturnBuilder items(List<SaleReturnItem> items) { this.items = items; return this; }

        public SaleReturn build() {
            return new SaleReturn(id, returnNumber, sale, refundAmount, returnReason, createdBy, createdAt, items);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReturnNumber() { return returnNumber; }
    public void setReturnNumber(String returnNumber) { this.returnNumber = returnNumber; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<SaleReturnItem> getItems() { return items; }
    public void setItems(List<SaleReturnItem> items) { this.items = items; }
}
