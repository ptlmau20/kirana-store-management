package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(name = "gst_number", length = 30)
    private String gstNumber;

    @Column(length = 255)
    private String address;

    @Column(name = "balance_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAmount = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Supplier() {}

    public Supplier(Long id, String name, String contactPerson, String phone, String email, String gstNumber, String address, BigDecimal balanceAmount, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.gstNumber = gstNumber;
        this.address = address;
        this.balanceAmount = balanceAmount != null ? balanceAmount : BigDecimal.ZERO;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (balanceAmount == null) balanceAmount = BigDecimal.ZERO;
    }

    public static SupplierBuilder builder() { return new SupplierBuilder(); }

    public static class SupplierBuilder {
        private Long id;
        private String name;
        private String contactPerson;
        private String phone;
        private String email;
        private String gstNumber;
        private String address;
        private BigDecimal balanceAmount = BigDecimal.ZERO;
        private LocalDateTime createdAt;

        public SupplierBuilder id(Long id) { this.id = id; return this; }
        public SupplierBuilder name(String name) { this.name = name; return this; }
        public SupplierBuilder contactPerson(String contactPerson) { this.contactPerson = contactPerson; return this; }
        public SupplierBuilder phone(String phone) { this.phone = phone; return this; }
        public SupplierBuilder email(String email) { this.email = email; return this; }
        public SupplierBuilder gstNumber(String gstNumber) { this.gstNumber = gstNumber; return this; }
        public SupplierBuilder address(String address) { this.address = address; return this; }
        public SupplierBuilder balanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; return this; }
        public SupplierBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Supplier build() {
            return new Supplier(id, name, contactPerson, phone, email, gstNumber, address, balanceAmount, createdAt);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
