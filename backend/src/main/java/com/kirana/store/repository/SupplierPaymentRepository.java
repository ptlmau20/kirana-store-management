package com.kirana.store.repository;

import com.kirana.store.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Long> {

    List<SupplierPayment> findBySupplierIdOrderByCreatedAtDesc(Long supplierId);

    List<SupplierPayment> findAllByOrderByCreatedAtDesc();
}
