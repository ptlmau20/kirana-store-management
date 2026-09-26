package com.kirana.store.repository;

import com.kirana.store.entity.PurchaseReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {

    Optional<PurchaseReturn> findByReturnNumber(String returnNumber);

    List<PurchaseReturn> findByPurchaseId(Long purchaseId);

    List<PurchaseReturn> findAllByOrderByCreatedAtDesc();
}
