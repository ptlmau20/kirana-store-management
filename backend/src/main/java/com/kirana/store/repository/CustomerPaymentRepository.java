package com.kirana.store.repository;

import com.kirana.store.entity.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {

    List<CustomerPayment> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    boolean existsByCustomerId(Long customerId);

    List<CustomerPayment> findAllByOrderByCreatedAtDesc();
}
