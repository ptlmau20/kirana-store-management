package com.kirana.store.repository;

import com.kirana.store.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPhone(String phone);

    Boolean existsByPhone(String phone);

    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) OR c.phone LIKE CONCAT('%', :query, '%')")
    List<Customer> searchCustomers(@Param("query") String query);

    @Query("SELECT c FROM Customer c WHERE c.creditBalance > 0 ORDER BY c.creditBalance DESC")
    List<Customer> findCustomersWithUdhar();

    @Query("SELECT SUM(c.creditBalance) FROM Customer c")
    BigDecimal getTotalUdharOutstanding();
}
