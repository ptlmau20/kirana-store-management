package com.kirana.store.repository;

import com.kirana.store.entity.LoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {

    List<LoginAudit> findTop100ByOrderByCreatedAtDesc();

    List<LoginAudit> findByUsernameOrderByCreatedAtDesc(String username);
}
