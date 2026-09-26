package com.kirana.store.service;

import com.kirana.store.dto.DashboardSummaryDto;
import com.kirana.store.dto.ProductDto;
import com.kirana.store.dto.SaleDto;
import com.kirana.store.dto.UserDto;
import com.kirana.store.entity.LoginAudit;
import com.kirana.store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final ProductService productService;
    private final BillingService billingService;

    public ReportService(SaleRepository saleRepository, ProductRepository productRepository, CustomerRepository customerRepository, SupplierRepository supplierRepository, LoginAuditRepository loginAuditRepository, ProductService productService, BillingService billingService) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.productService = productService;
        this.billingService = billingService;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryDto getDashboardSummary() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);

        BigDecimal todaySales = saleRepository.getTotalSalesBetween(todayStart, todayEnd);
        if (todaySales == null) todaySales = BigDecimal.ZERO;

        Long todayOrders = saleRepository.getCountSalesBetween(todayStart, todayEnd);
        if (todayOrders == null) todayOrders = 0L;

        BigDecimal monthlySales = saleRepository.getTotalSalesBetween(monthStart, todayEnd);
        if (monthlySales == null) monthlySales = BigDecimal.ZERO;

        Long monthlyOrders = saleRepository.getCountSalesBetween(monthStart, todayEnd);
        if (monthlyOrders == null) monthlyOrders = 0L;

        Long activeProducts = productRepository.countByActiveTrue();
        Long lowStockCount = productRepository.countLowStockProducts();

        Long totalCustomers = customerRepository.count();
        BigDecimal udharOutstanding = customerRepository.getTotalUdharOutstanding();
        if (udharOutstanding == null) udharOutstanding = BigDecimal.ZERO;

        BigDecimal supplierPayables = supplierRepository.getTotalSupplierPayable();
        if (supplierPayables == null) supplierPayables = BigDecimal.ZERO;

        List<SaleDto.Response> recentSales = saleRepository.findAllByOrderByCreatedAtDesc().stream()
                .limit(10)
                .map(billingService::mapToResponse)
                .collect(Collectors.toList());

        List<ProductDto.Response> lowStockProducts = productService.getLowStockProducts();

        return DashboardSummaryDto.builder()
                .todaySalesAmount(todaySales)
                .todayOrdersCount(todayOrders)
                .monthlySalesAmount(monthlySales)
                .monthlyOrdersCount(monthlyOrders)
                .totalActiveProducts(activeProducts)
                .lowStockCount(lowStockCount)
                .totalCustomersCount(totalCustomers)
                .totalUdharOutstanding(udharOutstanding)
                .totalSupplierPayables(supplierPayables)
                .recentSales(recentSales)
                .lowStockProducts(lowStockProducts)
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserDto.AuditLog> getAuditLogs() {
        return loginAuditRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(this::mapToAuditLog)
                .collect(Collectors.toList());
    }

    private UserDto.AuditLog mapToAuditLog(LoginAudit audit) {
        return UserDto.AuditLog.builder()
                .id(audit.getId())
                .userId(audit.getUserId())
                .username(audit.getUsername())
                .eventType(audit.getEventType())
                .ipAddress(audit.getIpAddress())
                .userAgent(audit.getUserAgent())
                .success(audit.getSuccess())
                .createdAt(audit.getCreatedAt())
                .build();
    }
}
