package com.kirana.store.controller;

import com.kirana.store.dto.DashboardSummaryDto;
import com.kirana.store.dto.UserDto;
import com.kirana.store.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary() {
        return ResponseEntity.ok(reportService.getDashboardSummary());
    }

    @GetMapping("/audit-logs")
    public ResponseEntity<List<UserDto.AuditLog>> getAuditLogs() {
        return ResponseEntity.ok(reportService.getAuditLogs());
    }
}
