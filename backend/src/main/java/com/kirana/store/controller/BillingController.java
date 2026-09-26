package com.kirana.store.controller;

import com.kirana.store.dto.SaleDto;
import com.kirana.store.service.BillingService;
import com.kirana.store.service.InvoicePdfService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;
    private final InvoicePdfService invoicePdfService;

    public BillingController(BillingService billingService, InvoicePdfService invoicePdfService) {
        this.billingService = billingService;
        this.invoicePdfService = invoicePdfService;
    }

    @PostMapping
    public ResponseEntity<SaleDto.Response> createSale(
            @Valid @RequestBody SaleDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails) {
        SaleDto.Response response = billingService.createSale(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SaleDto.Response>> getAllSales() {
        return ResponseEntity.ok(billingService.getAllSales());
    }

    @GetMapping("/sales")
    public ResponseEntity<Page<SaleDto.Response>> getSales(
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(billingService.getSales(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto.Response> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getSaleById(id));
    }

    @GetMapping("/bill-number/{billNumber}")
    public ResponseEntity<SaleDto.Response> getSaleByBillNumber(@PathVariable String billNumber) {
        return ResponseEntity.ok(billingService.getSaleByBillNumber(billNumber));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSale(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        billingService.deleteSale(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
        SaleDto.Response sale = billingService.getSaleById(id);
        byte[] pdfBytes = invoicePdfService.generateInvoicePdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "Invoice-" + sale.getBillNumber() + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

}
