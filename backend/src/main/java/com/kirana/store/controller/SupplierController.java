package com.kirana.store.controller;

import com.kirana.store.dto.SupplierDto;
import com.kirana.store.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<SupplierDto.Response>> searchSuppliers(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(supplierService.searchSuppliers(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto.Response> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto.Response> createSupplier(@Valid @RequestBody SupplierDto.Request request) {
        SupplierDto.Response created = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto.Response> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierDto.Request request) {
        SupplierDto.Response updated = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/payments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SupplierDto.PaymentResponse> recordSupplierPayment(
            @Valid @RequestBody SupplierDto.PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        SupplierDto.PaymentResponse payment = supplierService.recordSupplierPayment(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<SupplierDto.PaymentResponse>> getSupplierPaymentHistory(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierPaymentHistory(id));
    }
}
