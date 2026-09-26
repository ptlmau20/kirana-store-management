package com.kirana.store.controller;

import com.kirana.store.dto.CustomerDto;
import com.kirana.store.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto.Response>> searchCustomers(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(customerService.searchCustomers(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto.Response> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDto.Response> getCustomerByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(customerService.getCustomerByPhone(phone));
    }

    @GetMapping("/udhar")
    public ResponseEntity<List<CustomerDto.Response>> getCustomersWithUdhar() {
        return ResponseEntity.ok(customerService.getCustomersWithUdhar());
    }

    @PostMapping
    public ResponseEntity<CustomerDto.Response> createCustomer(@Valid @RequestBody CustomerDto.Request request) {
        CustomerDto.Response created = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto.Response> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto.Request request) {
        CustomerDto.Response updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/udhar-adjustments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDto.Response> adjustUdhar(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto.UdharAdjustmentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(customerService.adjustUdhar(id, request, userDetails.getUsername()));
    }

    @PostMapping("/payments")
    public ResponseEntity<CustomerDto.PaymentResponse> recordCustomerPayment(
            @Valid @RequestBody CustomerDto.PaymentRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CustomerDto.PaymentResponse payment = customerService.recordCustomerPayment(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<List<CustomerDto.PaymentResponse>> getCustomerPaymentHistory(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerPaymentHistory(id));
    }
}
