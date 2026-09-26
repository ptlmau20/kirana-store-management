package com.kirana.store.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardSummaryDto {
    private BigDecimal todaySalesAmount;
    private Long todayOrdersCount;
    private BigDecimal monthlySalesAmount;
    private Long monthlyOrdersCount;
    private Long totalActiveProducts;
    private Long lowStockCount;
    private Long totalCustomersCount;
    private BigDecimal totalUdharOutstanding;
    private BigDecimal totalSupplierPayables;
    private List<SaleDto.Response> recentSales;
    private List<ProductDto.Response> lowStockProducts;

    public DashboardSummaryDto() {}

    public DashboardSummaryDto(BigDecimal todaySalesAmount, Long todayOrdersCount, BigDecimal monthlySalesAmount, Long monthlyOrdersCount, Long totalActiveProducts, Long lowStockCount, Long totalCustomersCount, BigDecimal totalUdharOutstanding, BigDecimal totalSupplierPayables, List<SaleDto.Response> recentSales, List<ProductDto.Response> lowStockProducts) {
        this.todaySalesAmount = todaySalesAmount;
        this.todayOrdersCount = todayOrdersCount;
        this.monthlySalesAmount = monthlySalesAmount;
        this.monthlyOrdersCount = monthlyOrdersCount;
        this.totalActiveProducts = totalActiveProducts;
        this.lowStockCount = lowStockCount;
        this.totalCustomersCount = totalCustomersCount;
        this.totalUdharOutstanding = totalUdharOutstanding;
        this.totalSupplierPayables = totalSupplierPayables;
        this.recentSales = recentSales;
        this.lowStockProducts = lowStockProducts;
    }

    public static DashboardSummaryDtoBuilder builder() { return new DashboardSummaryDtoBuilder(); }

    public static class DashboardSummaryDtoBuilder {
        private BigDecimal todaySalesAmount;
        private Long todayOrdersCount;
        private BigDecimal monthlySalesAmount;
        private Long monthlyOrdersCount;
        private Long totalActiveProducts;
        private Long lowStockCount;
        private Long totalCustomersCount;
        private BigDecimal totalUdharOutstanding;
        private BigDecimal totalSupplierPayables;
        private List<SaleDto.Response> recentSales;
        private List<ProductDto.Response> lowStockProducts;

        public DashboardSummaryDtoBuilder todaySalesAmount(BigDecimal todaySalesAmount) { this.todaySalesAmount = todaySalesAmount; return this; }
        public DashboardSummaryDtoBuilder todayOrdersCount(Long todayOrdersCount) { this.todayOrdersCount = todayOrdersCount; return this; }
        public DashboardSummaryDtoBuilder monthlySalesAmount(BigDecimal monthlySalesAmount) { this.monthlySalesAmount = monthlySalesAmount; return this; }
        public DashboardSummaryDtoBuilder monthlyOrdersCount(Long monthlyOrdersCount) { this.monthlyOrdersCount = monthlyOrdersCount; return this; }
        public DashboardSummaryDtoBuilder totalActiveProducts(Long totalActiveProducts) { this.totalActiveProducts = totalActiveProducts; return this; }
        public DashboardSummaryDtoBuilder lowStockCount(Long lowStockCount) { this.lowStockCount = lowStockCount; return this; }
        public DashboardSummaryDtoBuilder totalCustomersCount(Long totalCustomersCount) { this.totalCustomersCount = totalCustomersCount; return this; }
        public DashboardSummaryDtoBuilder totalUdharOutstanding(BigDecimal totalUdharOutstanding) { this.totalUdharOutstanding = totalUdharOutstanding; return this; }
        public DashboardSummaryDtoBuilder totalSupplierPayables(BigDecimal totalSupplierPayables) { this.totalSupplierPayables = totalSupplierPayables; return this; }
        public DashboardSummaryDtoBuilder recentSales(List<SaleDto.Response> recentSales) { this.recentSales = recentSales; return this; }
        public DashboardSummaryDtoBuilder lowStockProducts(List<ProductDto.Response> lowStockProducts) { this.lowStockProducts = lowStockProducts; return this; }

        public DashboardSummaryDto build() {
            return new DashboardSummaryDto(todaySalesAmount, todayOrdersCount, monthlySalesAmount, monthlyOrdersCount, totalActiveProducts, lowStockCount, totalCustomersCount, totalUdharOutstanding, totalSupplierPayables, recentSales, lowStockProducts);
        }
    }

    public BigDecimal getTodaySalesAmount() { return todaySalesAmount; }
    public void setTodaySalesAmount(BigDecimal todaySalesAmount) { this.todaySalesAmount = todaySalesAmount; }
    public Long getTodayOrdersCount() { return todayOrdersCount; }
    public void setTodayOrdersCount(Long todayOrdersCount) { this.todayOrdersCount = todayOrdersCount; }
    public BigDecimal getMonthlySalesAmount() { return monthlySalesAmount; }
    public void setMonthlySalesAmount(BigDecimal monthlySalesAmount) { this.monthlySalesAmount = monthlySalesAmount; }
    public Long getMonthlyOrdersCount() { return monthlyOrdersCount; }
    public void setMonthlyOrdersCount(Long monthlyOrdersCount) { this.monthlyOrdersCount = monthlyOrdersCount; }
    public Long getTotalActiveProducts() { return totalActiveProducts; }
    public void setTotalActiveProducts(Long totalActiveProducts) { this.totalActiveProducts = totalActiveProducts; }
    public Long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(Long lowStockCount) { this.lowStockCount = lowStockCount; }
    public Long getTotalCustomersCount() { return totalCustomersCount; }
    public void setTotalCustomersCount(Long totalCustomersCount) { this.totalCustomersCount = totalCustomersCount; }
    public BigDecimal getTotalUdharOutstanding() { return totalUdharOutstanding; }
    public void setTotalUdharOutstanding(BigDecimal totalUdharOutstanding) { this.totalUdharOutstanding = totalUdharOutstanding; }
    public BigDecimal getTotalSupplierPayables() { return totalSupplierPayables; }
    public void setTotalSupplierPayables(BigDecimal totalSupplierPayables) { this.totalSupplierPayables = totalSupplierPayables; }
    public List<SaleDto.Response> getRecentSales() { return recentSales; }
    public void setRecentSales(List<SaleDto.Response> recentSales) { this.recentSales = recentSales; }
    public List<ProductDto.Response> getLowStockProducts() { return lowStockProducts; }
    public void setLowStockProducts(List<ProductDto.Response> lowStockProducts) { this.lowStockProducts = lowStockProducts; }
}
