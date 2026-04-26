package com.gary.fraudflagger.repository;

import com.gary.fraudflagger.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByTimestampDesc(); //will return all entries newest first - no sql needed. Spring boot figures out the query automatically.
}
