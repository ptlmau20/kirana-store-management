package com.kirana.store.controller;

import com.kirana.store.dto.ReturnDto;
import com.kirana.store.service.ReturnService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    private final ReturnService returnService;

    public ReturnController(ReturnService returnService) {
        this.returnService = returnService;
    }

    @GetMapping("/sales")
    public ResponseEntity<List<ReturnDto.SaleReturnResponse>> getAllSaleReturns() {
        return ResponseEntity.ok(returnService.getAllSaleReturns());
    }

    @PostMapping("/sales")
    public ResponseEntity<ReturnDto.SaleReturnResponse> createSaleReturn(
            @Valid @RequestBody ReturnDto.SaleReturnRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ReturnDto.SaleReturnResponse created = returnService.createSaleReturn(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/purchases")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReturnDto.PurchaseReturnResponse>> getAllPurchaseReturns() {
        return ResponseEntity.ok(returnService.getAllPurchaseReturns());
    }

    @PostMapping("/purchases")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReturnDto.PurchaseReturnResponse> createPurchaseReturn(
            @Valid @RequestBody ReturnDto.PurchaseReturnRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        ReturnDto.PurchaseReturnResponse created = returnService.createPurchaseReturn(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
