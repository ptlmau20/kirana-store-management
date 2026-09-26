package com.kirana.store.repository;

import com.kirana.store.entity.SaleReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleReturnRepository extends JpaRepository<SaleReturn, Long> {

    Optional<SaleReturn> findByReturnNumber(String returnNumber);

    List<SaleReturn> findBySaleId(Long saleId);

    boolean existsBySaleId(Long saleId);

    List<SaleReturn> findAllByOrderByCreatedAtDesc();
}
