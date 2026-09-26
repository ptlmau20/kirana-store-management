package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String address;

    @Column(name = "total_purchases", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPurchases = BigDecimal.ZERO;

    @Column(name = "credit_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditBalance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Customer() {}

    public Customer(Long id, String name, String phone, String email, String address, BigDecimal totalPurchases, BigDecimal creditBalance, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.totalPurchases = totalPurchases != null ? totalPurchases : BigDecimal.ZERO;
        this.creditBalance = creditBalance != null ? creditBalance : BigDecimal.ZERO;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (totalPurchases == null) totalPurchases = BigDecimal.ZERO;
        if (creditBalance == null) creditBalance = BigDecimal.ZERO;
    }

    public static CustomerBuilder builder() { return new CustomerBuilder(); }

    public static class CustomerBuilder {
        private Long id;
        private String name;
        private String phone;
        private String email;
        private String address;
        private BigDecimal totalPurchases = BigDecimal.ZERO;
        private BigDecimal creditBalance = BigDecimal.ZERO;
        private LocalDateTime createdAt;

        public CustomerBuilder id(Long id) { this.id = id; return this; }
        public CustomerBuilder name(String name) { this.name = name; return this; }
        public CustomerBuilder phone(String phone) { this.phone = phone; return this; }
        public CustomerBuilder email(String email) { this.email = email; return this; }
        public CustomerBuilder address(String address) { this.address = address; return this; }
        public CustomerBuilder totalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; return this; }
        public CustomerBuilder creditBalance(BigDecimal creditBalance) { this.creditBalance = creditBalance; return this; }
        public CustomerBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Customer build() {
            return new Customer(id, name, phone, email, address, totalPurchases, creditBalance, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }

    public BigDecimal getCreditBalance() { return creditBalance; }
    public void setCreditBalance(BigDecimal creditBalance) { this.creditBalance = creditBalance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
