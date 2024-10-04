package com.secure.docs.repository;

import com.secure.docs.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {
    List<AuditLog> findByNoteId(Long noteId);
}
