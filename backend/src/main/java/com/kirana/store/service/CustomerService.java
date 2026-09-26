package com.kirana.store.service;

import com.kirana.store.dto.CustomerDto;
import com.kirana.store.entity.Customer;
import com.kirana.store.entity.CustomerPayment;
import com.kirana.store.entity.UdharAdjustment;
import com.kirana.store.exception.BadRequestException;
import com.kirana.store.exception.ResourceNotFoundException;
import com.kirana.store.repository.CustomerPaymentRepository;
import com.kirana.store.repository.CustomerRepository;
import com.kirana.store.repository.SaleRepository;
import com.kirana.store.repository.UdharAdjustmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final SaleRepository saleRepository;
    private final UdharAdjustmentRepository udharAdjustmentRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerPaymentRepository customerPaymentRepository,
                           SaleRepository saleRepository, UdharAdjustmentRepository udharAdjustmentRepository) {
        this.customerRepository = customerRepository;
        this.customerPaymentRepository = customerPaymentRepository;
        this.saleRepository = saleRepository;
        this.udharAdjustmentRepository = udharAdjustmentRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerDto.Response> searchCustomers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return customerRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
        return customerRepository.searchCustomers(query.trim()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDto.Response getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapToResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerDto.Response getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with phone: " + phone));
        return mapToResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto.Response> getCustomersWithUdhar() {
        return customerRepository.findCustomersWithUdhar().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDto.Response createCustomer(CustomerDto.Request request) {
        if (customerRepository.existsByPhone(request.getPhone().trim())) {
            throw new BadRequestException("Customer with phone number '" + request.getPhone() + "' already exists");
        }

        Customer customer = Customer.builder()
                .name(request.getName().trim())
                .phone(request.getPhone().trim())
                .email(request.getEmail() != null ? request.getEmail().trim() : null)
                .address(request.getAddress() != null ? request.getAddress().trim() : null)
                .totalPurchases(BigDecimal.ZERO)
                .creditBalance(BigDecimal.ZERO)
                .build();

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Transactional
    public CustomerDto.Response updateCustomer(Long id, CustomerDto.Request request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (!customer.getPhone().equals(request.getPhone().trim()) &&
            customerRepository.existsByPhone(request.getPhone().trim())) {
            throw new BadRequestException("Phone number '" + request.getPhone() + "' is already registered to another customer");
        }

        customer.setName(request.getName().trim());
        customer.setPhone(request.getPhone().trim());
        customer.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        customer.setAddress(request.getAddress() != null ? request.getAddress().trim() : null);

        Customer updated = customerRepository.save(customer);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        if (customer.getCreditBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Customer cannot be removed while Udhar balance is outstanding");
        }
        if (saleRepository.existsByCustomerId(id) || customerPaymentRepository.existsByCustomerId(id) ||
                udharAdjustmentRepository.existsByCustomerId(id)) {
            throw new BadRequestException("Customer has billing or Udhar history and cannot be removed");
        }

        customerRepository.delete(customer);
    }

    @Transactional
    public CustomerDto.Response adjustUdhar(Long id, CustomerDto.UdharAdjustmentRequest request, String performerName) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        BigDecimal delta = request.getBalanceDelta();
        if (delta == null || delta.compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("Udhar adjustment must be non-zero");
        }

        BigDecimal newBalance = customer.getCreditBalance().add(delta);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Udhar adjustment cannot make the balance negative");
        }

        customer.setCreditBalance(newBalance);
        customerRepository.save(customer);
        udharAdjustmentRepository.save(new UdharAdjustment(
                customer, delta, newBalance, request.getReason().trim(), performerName));
        return mapToResponse(customer);
    }

    @Transactional
    public CustomerDto.PaymentResponse recordCustomerPayment(CustomerDto.PaymentRequest request, String performerName) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        if (customer.getCreditBalance().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Customer '" + customer.getName() + "' does not have any outstanding Udhar/credit balance");
        }

        BigDecimal paymentAmount = request.getAmountPaid();
        if (paymentAmount.compareTo(customer.getCreditBalance()) > 0) {
            throw new BadRequestException("Repayment amount cannot exceed outstanding Udhar balance");
        }

        BigDecimal newCreditBalance = customer.getCreditBalance().subtract(paymentAmount);
        customer.setCreditBalance(newCreditBalance);
        customerRepository.save(customer);

        CustomerPayment payment = CustomerPayment.builder()
                .customer(customer)
                .amountPaid(paymentAmount)
                .paymentMethod(request.getPaymentMethod())
                .referenceNote(request.getReferenceNote())
                .createdBy(performerName)
                .build();

        CustomerPayment saved = customerPaymentRepository.save(payment);

        return CustomerDto.PaymentResponse.builder()
                .id(saved.getId())
                .customerId(customer.getId())
                .customerName(customer.getName())
                .amountPaid(saved.getAmountPaid())
                .paymentMethod(saved.getPaymentMethod())
                .referenceNote(saved.getReferenceNote())
                .createdBy(saved.getCreatedBy())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<CustomerDto.PaymentResponse> getCustomerPaymentHistory(Long customerId) {
        return customerPaymentRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(p -> CustomerDto.PaymentResponse.builder()
                        .id(p.getId())
                        .customerId(p.getCustomer().getId())
                        .customerName(p.getCustomer().getName())
                        .amountPaid(p.getAmountPaid())
                        .paymentMethod(p.getPaymentMethod())
                        .referenceNote(p.getReferenceNote())
                        .createdBy(p.getCreatedBy())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public CustomerDto.Response mapToResponse(Customer c) {
        return CustomerDto.Response.builder()
                .id(c.getId())
                .name(c.getName())
                .phone(c.getPhone())
                .email(c.getEmail())
                .address(c.getAddress())
                .totalPurchases(c.getTotalPurchases())
                .creditBalance(c.getCreditBalance())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
