package com.kirana.store.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    public enum MovementType {
        SALE,
        PURCHASE,
        SALES_RETURN,
        PURCHASE_RETURN,
        ADJUSTMENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 30)
    private MovementType movementType;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @Column(name = "previous_stock", nullable = false)
    private Integer previousStock;

    @Column(name = "new_stock", nullable = false)
    private Integer newStock;

    @Column(name = "reference_id", length = 100)
    private String referenceId;

    @Column(length = 255)
    private String notes;

    @Column(name = "created_by_name", length = 100)
    private String createdByName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public StockMovement() {}

    public StockMovement(Long id, Product product, MovementType movementType, Integer quantityChange, Integer previousStock, Integer newStock, String referenceId, String notes, String createdByName, LocalDateTime createdAt) {
        this.id = id;
        this.product = product;
        this.movementType = movementType;
        this.quantityChange = quantityChange;
        this.previousStock = previousStock;
        this.newStock = newStock;
        this.referenceId = referenceId;
        this.notes = notes;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static StockMovementBuilder builder() { return new StockMovementBuilder(); }

    public static class StockMovementBuilder {
        private Long id;
        private Product product;
        private MovementType movementType;
        private Integer quantityChange;
        private Integer previousStock;
        private Integer newStock;
        private String referenceId;
        private String notes;
        private String createdByName;
        private LocalDateTime createdAt;

        public StockMovementBuilder id(Long id) { this.id = id; return this; }
        public StockMovementBuilder product(Product product) { this.product = product; return this; }
        public StockMovementBuilder movementType(MovementType movementType) { this.movementType = movementType; return this; }
        public StockMovementBuilder quantityChange(Integer quantityChange) { this.quantityChange = quantityChange; return this; }
        public StockMovementBuilder previousStock(Integer previousStock) { this.previousStock = previousStock; return this; }
        public StockMovementBuilder newStock(Integer newStock) { this.newStock = newStock; return this; }
        public StockMovementBuilder referenceId(String referenceId) { this.referenceId = referenceId; return this; }
        public StockMovementBuilder notes(String notes) { this.notes = notes; return this; }
        public StockMovementBuilder createdByName(String createdByName) { this.createdByName = createdByName; return this; }
        public StockMovementBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public StockMovement build() {
            return new StockMovement(id, product, movementType, quantityChange, previousStock, newStock, referenceId, notes, createdByName, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public Integer getQuantityChange() { return quantityChange; }
    public void setQuantityChange(Integer quantityChange) { this.quantityChange = quantityChange; }

    public Integer getPreviousStock() { return previousStock; }
    public void setPreviousStock(Integer previousStock) { this.previousStock = previousStock; }

    public Integer getNewStock() { return newStock; }
    public void setNewStock(Integer newStock) { this.newStock = newStock; }

    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
