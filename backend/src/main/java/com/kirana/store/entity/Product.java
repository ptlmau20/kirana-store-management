package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String barcode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "hsn_code", length = 20)
    private String hsnCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal mrp;

    @Column(name = "gst_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal gstRate;

    @Column(nullable = false, length = 20)
    private String unit;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "min_stock_alert", nullable = false)
    private Integer minStockAlert;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product() {}

    public Product(Long id, String barcode, String name, String hsnCode, Category category, BigDecimal costPrice, BigDecimal sellingPrice, BigDecimal mrp, BigDecimal gstRate, String unit, Integer stockQuantity, Integer minStockAlert, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.hsnCode = hsnCode;
        this.category = category;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.mrp = mrp;
        this.gstRate = gstRate != null ? gstRate : BigDecimal.ZERO;
        this.unit = unit;
        this.stockQuantity = stockQuantity != null ? stockQuantity : 0;
        this.minStockAlert = minStockAlert != null ? minStockAlert : 5;
        this.active = active != null ? active : true;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) active = true;
        if (stockQuantity == null) stockQuantity = 0;
        if (minStockAlert == null) minStockAlert = 5;
        if (gstRate == null) gstRate = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static ProductBuilder builder() { return new ProductBuilder(); }

    public static class ProductBuilder {
        private Long id;
        private String barcode;
        private String name;
        private String hsnCode;
        private Category category;
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private BigDecimal mrp;
        private BigDecimal gstRate;
        private String unit;
        private Integer stockQuantity;
        private Integer minStockAlert;
        private Boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public ProductBuilder id(Long id) { this.id = id; return this; }
        public ProductBuilder barcode(String barcode) { this.barcode = barcode; return this; }
        public ProductBuilder name(String name) { this.name = name; return this; }
        public ProductBuilder hsnCode(String hsnCode) { this.hsnCode = hsnCode; return this; }
        public ProductBuilder category(Category category) { this.category = category; return this; }
        public ProductBuilder costPrice(BigDecimal costPrice) { this.costPrice = costPrice; return this; }
        public ProductBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
        public ProductBuilder mrp(BigDecimal mrp) { this.mrp = mrp; return this; }
        public ProductBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
        public ProductBuilder unit(String unit) { this.unit = unit; return this; }
        public ProductBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
        public ProductBuilder minStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; return this; }
        public ProductBuilder active(Boolean active) { this.active = active; return this; }
        public ProductBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ProductBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Product build() {
            return new Product(id, barcode, name, hsnCode, category, costPrice, sellingPrice, mrp, gstRate, unit, stockQuantity, minStockAlert, active, createdAt, updatedAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHsnCode() { return hsnCode; }
    public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public BigDecimal getGstRate() { return gstRate; }
    public void setGstRate(BigDecimal gstRate) { this.gstRate = gstRate; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Integer getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
