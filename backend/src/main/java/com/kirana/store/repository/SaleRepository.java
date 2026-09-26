package com.kirana.store.repository;

import com.kirana.store.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByBillNumber(String billNumber);

    List<Sale> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean existsByCustomerId(Long customerId);

    List<Sale> findAllByOrderByCreatedAtDesc();

    List<Sale> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(s.netAmount) FROM Sale s WHERE s.createdAt >= :start AND s.createdAt <= :end")
    BigDecimal getTotalSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.createdAt >= :start AND s.createdAt <= :end")
    Long getCountSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
