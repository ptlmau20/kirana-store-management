package com.kirana.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "purchase_items")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false)
    @JsonIgnore
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "unit_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "gst_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal gstRate;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    public PurchaseItem() {}

    public PurchaseItem(Long id, Purchase purchase, Product product, BigDecimal unitCost, Integer quantity, BigDecimal gstRate, BigDecimal totalAmount) {
        this.id = id;
        this.purchase = purchase;
        this.product = product;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.gstRate = gstRate;
        this.totalAmount = totalAmount;
    }

    public static PurchaseItemBuilder builder() { return new PurchaseItemBuilder(); }

    public static class PurchaseItemBuilder {
        private Long id;
        private Purchase purchase;
        private Product product;
        private BigDecimal unitCost;
        private Integer quantity;
        private BigDecimal gstRate;
        private BigDecimal totalAmount;

        public PurchaseItemBuilder id(Long id) { this.id = id; return this; }
        public PurchaseItemBuilder purchase(Purchase purchase) { this.purchase = purchase; return this; }
        public PurchaseItemBuilder product(Product product) { this.product = product; return this; }
        public PurchaseItemBuilder unitCost(BigDecimal unitCost) { this.unitCost = unitCost; return this; }
        public PurchaseItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public PurchaseItemBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
        public PurchaseItemBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }

        public PurchaseItem build() {
            return new PurchaseItem(id, purchase, product, unitCost, quantity, gstRate, totalAmount);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
