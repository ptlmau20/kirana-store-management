package com.kirana.store.repository;

import com.kirana.store.entity.UdharAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UdharAdjustmentRepository extends JpaRepository<UdharAdjustment, Long> {

    boolean existsByCustomerId(Long customerId);
}