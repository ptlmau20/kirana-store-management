package com.kirana.store.repository;

import com.kirana.store.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findTop100ByOrderByCreatedAtDesc();

    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);
}
