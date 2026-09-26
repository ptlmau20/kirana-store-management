package com.kirana.store.repository;

import com.kirana.store.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR s.phone LIKE CONCAT('%', :query, '%')")
    List<Supplier> searchSuppliers(@Param("query") String query);

    @Query("SELECT SUM(s.balanceAmount) FROM Supplier s")
    BigDecimal getTotalSupplierPayable();
}
