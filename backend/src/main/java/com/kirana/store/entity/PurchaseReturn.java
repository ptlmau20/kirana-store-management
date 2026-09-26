package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_returns")
public class PurchaseReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "return_number", nullable = false, unique = true, length = 50)
    private String returnNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "return_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal returnAmount;

    @Column(length = 255)
    private String reason;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PurchaseReturn() {}

    public PurchaseReturn(Long id, String returnNumber, Purchase purchase, Product product, Integer quantity, BigDecimal returnAmount, String reason, String createdBy, LocalDateTime createdAt) {
        this.id = id;
        this.returnNumber = returnNumber;
        this.purchase = purchase;
        this.product = product;
        this.quantity = quantity;
        this.returnAmount = returnAmount;
        this.reason = reason;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static PurchaseReturnBuilder builder() { return new PurchaseReturnBuilder(); }

    public static class PurchaseReturnBuilder {
        private Long id;
        private String returnNumber;
        private Purchase purchase;
        private Product product;
        private Integer quantity;
        private BigDecimal returnAmount;
        private String reason;
        private String createdBy;
        private LocalDateTime createdAt;

        public PurchaseReturnBuilder id(Long id) { this.id = id; return this; }
        public PurchaseReturnBuilder returnNumber(String returnNumber) { this.returnNumber = returnNumber; return this; }
        public PurchaseReturnBuilder purchase(Purchase purchase) { this.purchase = purchase; return this; }
        public PurchaseReturnBuilder product(Product product) { this.product = product; return this; }
        public PurchaseReturnBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public PurchaseReturnBuilder returnAmount(BigDecimal returnAmount) { this.returnAmount = returnAmount; return this; }
        public PurchaseReturnBuilder reason(String reason) { this.reason = reason; return this; }
        public PurchaseReturnBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public PurchaseReturnBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public PurchaseReturn build() {
            return new PurchaseReturn(id, returnNumber, purchase, product, quantity, returnAmount, reason, createdBy, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReturnNumber() { return returnNumber; }
    public void setReturnNumber(String returnNumber) { this.returnNumber = returnNumber; }

    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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
