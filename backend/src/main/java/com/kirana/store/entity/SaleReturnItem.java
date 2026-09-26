package com.kirana.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "sale_return_items")
public class SaleReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_return_id", nullable = false)
    @JsonIgnore
    private SaleReturn saleReturn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "refund_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal refundUnitPrice;

    @Column(name = "item_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal itemTotal;

    public SaleReturnItem() {}

    public SaleReturnItem(Long id, SaleReturn saleReturn, Product product, Integer quantity, BigDecimal refundUnitPrice, BigDecimal itemTotal) {
        this.id = id;
        this.saleReturn = saleReturn;
        this.product = product;
        this.quantity = quantity;
        this.refundUnitPrice = refundUnitPrice;
        this.itemTotal = itemTotal;
    }

    public static SaleReturnItemBuilder builder() { return new SaleReturnItemBuilder(); }

    public static class SaleReturnItemBuilder {
        private Long id;
        private SaleReturn saleReturn;
        private Product product;
        private Integer quantity;
        private BigDecimal refundUnitPrice;
        private BigDecimal itemTotal;

        public SaleReturnItemBuilder id(Long id) { this.id = id; return this; }
        public SaleReturnItemBuilder saleReturn(SaleReturn saleReturn) { this.saleReturn = saleReturn; return this; }
        public SaleReturnItemBuilder product(Product product) { this.product = product; return this; }
        public SaleReturnItemBuilder quantity(Integer quantity) { this.quantity = quantity; return this; }
        public SaleReturnItemBuilder refundUnitPrice(BigDecimal refundUnitPrice) { this.refundUnitPrice = refundUnitPrice; return this; }
        public SaleReturnItemBuilder itemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; return this; }

        public SaleReturnItem build() {
            return new SaleReturnItem(id, saleReturn, product, quantity, refundUnitPrice, itemTotal);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SaleReturn getSaleReturn() { return saleReturn; }
    public void setSaleReturn(SaleReturn saleReturn) { this.saleReturn = saleReturn; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getRefundUnitPrice() { return refundUnitPrice; }
    public void setRefundUnitPrice(BigDecimal refundUnitPrice) { this.refundUnitPrice = refundUnitPrice; }

    public BigDecimal getItemTotal() { return itemTotal; }
    public void setItemTotal(BigDecimal itemTotal) { this.itemTotal = itemTotal; }
}
