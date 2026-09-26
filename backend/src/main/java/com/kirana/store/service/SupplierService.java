package com.kirana.store.service;

import com.kirana.store.dto.SupplierDto;
import com.kirana.store.entity.Supplier;
import com.kirana.store.entity.SupplierPayment;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.SupplierPaymentRepository;
import com.kirana.store.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;

    public SupplierService(SupplierRepository supplierRepository, SupplierPaymentRepository supplierPaymentRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
    }

    @Transactional(readOnly = true)
    public List<SupplierDto.Response> searchSuppliers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return supplierRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return supplierRepository.searchSuppliers(query.trim()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierDto.Response getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return mapToResponse(supplier);
    }

    @Transactional
    public SupplierDto.Response createSupplier(SupplierDto.Request request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName().trim())
                .contactPerson(request.getContactPerson() != null ? request.getContactPerson().trim() : null)
                .phone(request.getPhone().trim())
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .gstNumber(request.getGstNumber() != null ? request.getGstNumber().trim() : null)
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .balanceAmount(BigDecimal.ZERO)
                .build();

        Supplier saved = supplierRepository.save(supplier);
        return mapToResponse(saved);
    }

    @Transactional
    public SupplierDto.Response updateSupplier(Long id, SupplierDto.Request request) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setName(request.getName().trim());
        supplier.setContactPerson(request.getContactPerson() != null ? request.getContactPerson().trim() : null);
        supplier.setPhone(request.getPhone().trim());
        supplier.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        supplier.setGstNumber(request.getGstNumber() != null ? request.getGstNumber().trim() : null);
        supplier.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);

        Supplier updated = supplierRepository.save(supplier);
        return mapToResponse(updated);
    }

    @Transactional
    public SupplierDto.PaymentResponse recordSupplierPayment(SupplierDto.PaymentRequest request, String performerName) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + request.getSupplierId()));

        if (supplier.getBalanceAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Supplier '" + supplier.getName() + "' does not have any outstanding payable balance");
        }

        BigDecimal paymentAmount = request.getAmountPaid();
        BigDecimal newBalance = supplier.getBalanceAmount().subtract(paymentAmount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }

        supplier.setBalanceAmount(newBalance);
        supplierRepository.save(supplier);

        SupplierPayment payment = SupplierPayment.builder()
                .supplier(supplier)
                .amountPaid(paymentAmount)
                .paymentMethod(request.getPaymentMethod())
                .referenceNote(request.getReferenceNote())
                .createdBy(performerName)
                .build();

        SupplierPayment saved = supplierPaymentRepository.save(payment);

        return SupplierDto.PaymentResponse.builder()
                .id(saved.getId())
                .supplierId(supplier.getId())
                .supplierName(supplier.getName())
                .amountPaid(saved.getAmountPaid())
                .paymentMethod(saved.getPaymentMethod())
                .referenceNote(saved.getReferenceNote())
                .createdBy(saved.getCreatedBy())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SupplierDto.PaymentResponse> getSupplierPaymentHistory(Long supplierId) {
        return supplierPaymentRepository.findBySupplierIdOrderByCreatedAtDesc(supplierId).stream()
                .map(p -> SupplierDto.PaymentResponse.builder()
                        .id(p.getId())
                        .supplierId(p.getSupplier().getId())
                        .supplierName(p.getSupplier().getName())
                        .amountPaid(p.getAmountPaid())
                        .paymentMethod(p.getPaymentMethod())
                        .referenceNote(p.getReferenceNote())
                        .createdBy(p.getCreatedBy())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public SupplierDto.Response mapToResponse(Supplier s) {
        return SupplierDto.Response.builder()
                .id(s.getId())
                .name(s.getName())
                .contactPerson(s.getContactPerson())
                .phone(s.getPhone())
                .email(s.getEmail())
                .gstNumber(s.getGstNumber())
                .address(s.getAddress())
                .balanceAmount(s.getBalanceAmount())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
