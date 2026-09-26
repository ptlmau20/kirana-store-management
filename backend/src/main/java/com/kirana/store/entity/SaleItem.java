package com.kirana.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_items")
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @JsonIgnore
    private Sale sale;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal mrp;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "gst_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal gstRate;

    @Column(name = "item_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal itemTotal;

    public SaleItem() {}

    public SaleItem(Long id, Sale sale, Product product, BigDecimal unitPrice, BigDecimal mrp, Integer quantity, BigDecimal gstRate, BigDecimal itemTotal) {
        this.id = id;
        this.sale = sale;
        this.product = product;
        this.unitPrice = unitPrice;
        this.mrp = mrp;
        this.quantity = quantity;
        this.gstRate = gstRate;
        this.itemTotal = itemTotal;
    }

    public static SaleItemBuilder builder() { return new SaleItemBuilder(); }

    public static class SaleItemBuilder {
        private Long id;
        private Sale sale;
        private Product product;
        private BigDecimal unitPrice;
        private BigDecimal mrp;
        private Integer quantity;
        private BigDecimal gstRate;
        private BigDecimal itemTotal;

        public SaleItemBuilder id(Long id) { this.id = id; return this; }
        public SaleItemBuilder sale(Sale sale) { this.sale = sale; return this; }
        public SaleItemBuilder product(Product product) { this.product = product; return this; }
        public SaleItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
        public SaleItemBuilder mrp(BigDecimal mrp) { this.mrp = mrp; return this; }
        public SaleItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public SaleItemBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
        public SaleItemBuilder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }

        public SaleItem build() {
            return new SaleItem(id, sale, product, unitPrice, mrp, quantity, gstRate, itemTotal);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

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
