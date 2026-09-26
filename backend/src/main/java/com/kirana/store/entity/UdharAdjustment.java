package com.kirana.store.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "udhar_adjustments")
public class UdharAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "balance_delta", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceDelta;

    @Column(name = "balance_after", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAfter;

    @Column(nullable = false, length = 255)
    private String reason;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public UdharAdjustment() {}

    public UdharAdjustment(Customer customer, BigDecimal balanceDelta, BigDecimal balanceAfter, String reason, String createdBy) {
        this.customer = customer;
        this.balanceDelta = balanceDelta;
        this.balanceAfter = balanceAfter;
        this.reason = reason;
        this.createdBy = createdBy;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public BigDecimal getBalanceDelta() { return balanceDelta; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getReason() { return reason; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}