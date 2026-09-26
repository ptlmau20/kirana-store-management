package com.kirana.store.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDto {

    public static class Request {
        @NotBlank(message = "Product name is required")
        private String name;

        private String hsnCode;

        @NotNull(message = "Category is required")
        private Long categoryId;

        @NotNull(message = "Cost price is required")
        @PositiveOrZero(message = "Cost price must be zero or positive")
        private BigDecimal costPrice;

        @NotNull(message = "Selling price is required")
        @Positive(message = "Selling price must be positive")
        private BigDecimal sellingPrice;

        @NotNull(message = "MRP is required")
        @Positive(message = "MRP must be positive")
        private BigDecimal mrp;

        @NotNull(message = "GST rate is required")
        @PositiveOrZero(message = "GST rate must be zero or positive")
        private BigDecimal gstRate;

        @NotBlank(message = "Unit is required")
        private String unit;

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity cannot be negative")
        private Integer stockQuantity;

        @NotNull(message = "Minimum stock alert limit is required")
        @Min(value = 0, message = "Min stock alert limit cannot be negative")
        private Integer minStockAlert;

        private Boolean active;

        public Request() {}

        public Request(String name, String hsnCode, Long categoryId, BigDecimal costPrice, BigDecimal sellingPrice, BigDecimal mrp, BigDecimal gstRate, String unit, Integer stockQuantity, Integer minStockAlert, Boolean active) {
            this.name = name;
            this.hsnCode = hsnCode;
            this.categoryId = categoryId;
            this.costPrice = costPrice;
            this.sellingPrice = sellingPrice;
            this.mrp = mrp;
            this.gstRate = gstRate;
            this.unit = unit;
            this.stockQuantity = stockQuantity;
            this.minStockAlert = minStockAlert;
            this.active = active;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getHsnCode() { return hsnCode; }
        public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
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
    }

    public static class Response {
        private Long id;
        private String name;
        private String hsnCode;
        private Long categoryId;
        private String categoryName;
        private BigDecimal costPrice;
        private BigDecimal sellingPrice;
        private BigDecimal mrp;
        private BigDecimal gstRate;
        private String unit;
        private Integer stockQuantity;
        private Integer minStockAlert;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Response() {}

        public Response(Long id, String name, String hsnCode, Long categoryId, String categoryName, BigDecimal costPrice, BigDecimal sellingPrice, BigDecimal mrp, BigDecimal gstRate, String unit, Integer stockQuantity, Integer minStockAlert, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.id = id;
            this.name = name;
            this.hsnCode = hsnCode;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.costPrice = costPrice;
            this.sellingPrice = sellingPrice;
            this.mrp = mrp;
            this.gstRate = gstRate;
            this.unit = unit;
            this.stockQuantity = stockQuantity;
            this.minStockAlert = minStockAlert;
            this.active = active;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public static ResponseBuilder builder() { return new ResponseBuilder(); }

        public static class ResponseBuilder {
            private Long id;
            private String name;
            private String hsnCode;
            private Long categoryId;
            private String categoryName;
            private BigDecimal costPrice;
            private BigDecimal sellingPrice;
            private BigDecimal mrp;
            private BigDecimal gstRate;
            private String unit;
            private Integer stockQuantity;
            private Integer minStockAlert;
            private Boolean active;
            private LocalDateTime createdAt;
            private LocalDateTime updatedAt;

            public ResponseBuilder id(Long id) { this.id = id; return this; }
            public ResponseBuilder name(String name) { this.name = name; return this; }
            public ResponseBuilder hsnCode(String hsnCode) { this.hsnCode = hsnCode; return this; }
            public ResponseBuilder categoryId(Long categoryId) { this.categoryId = categoryId; return this; }
            public ResponseBuilder categoryName(String categoryName) { this.categoryName = categoryName; return this; }
            public ResponseBuilder costPrice(BigDecimal costPrice) { this.costPrice = costPrice; return this; }
            public ResponseBuilder sellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; return this; }
            public ResponseBuilder mrp(BigDecimal mrp) { this.mrp = mrp; return this; }
            public ResponseBuilder gstRate(BigDecimal gstRate) { this.gstRate = gstRate; return this; }
            public ResponseBuilder unit(String unit) { this.unit = unit; return this; }
            public ResponseBuilder stockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; return this; }
            public ResponseBuilder minStockAlert(Integer minStockAlert) { this.minStockAlert = minStockAlert; return this; }
            public ResponseBuilder active(Boolean active) { this.active = active; return this; }
            public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
            public ResponseBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

            public Response build() {
                return new Response(id, name, hsnCode, categoryId, categoryName, costPrice, sellingPrice, mrp, gstRate, unit, stockQuantity, minStockAlert, active, createdAt, updatedAt);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getHsnCode() { return hsnCode; }
        public void setHsnCode(String hsnCode) { this.hsnCode = hsnCode; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
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

    public static class StockAdjustmentRequest {
        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity change is required")
        private Integer quantityChange;

        @NotBlank(message = "Reason/Notes are required for stock adjustment")
        private String notes;

        public StockAdjustmentRequest() {}

        public StockAdjustmentRequest(Long productId, Integer quantityChange, String notes) {
            this.productId = productId;
            this.quantityChange = quantityChange;
            this.notes = notes;
        }

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantityChange() { return quantityChange; }
        public void setQuantityChange(Integer quantityChange) { this.quantityChange = quantityChange; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class CategoryDto {
        private Long id;

        @NotBlank(message = "Category name is required")
        private String name;

        private String description;

        public CategoryDto() {}

        public CategoryDto(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public static CategoryDtoBuilder builder() { return new CategoryDtoBuilder(); }

        public static class CategoryDtoBuilder {
            private Long id;
            private String name;
            private String description;

            public CategoryDtoBuilder id(Long id) { this.id = id; return this; }
            public CategoryDtoBuilder name(String name) { this.name = name; return this; }
            public CategoryDtoBuilder description(String description) { this.description = description; return this; }

            public CategoryDto build() {
                return new CategoryDto(id, name, description);
            }
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
