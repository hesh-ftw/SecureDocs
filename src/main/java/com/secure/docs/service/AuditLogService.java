package com.secure.docs.service;

import com.secure.docs.model.AuditLog;
import com.secure.docs.model.Note;
import com.secure.docs.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {
    @Autowired
    AuditLogRepository auditLogRepository;

    public void logNoteCreation(String username, Note note){
        AuditLog log= new AuditLog();
        log.setAction("CREATE");
        log.setUsername(username);
        log.setNoteId(note.getId());
        log.setNoteContent(note.getContent());
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    public void logNoteDeletion(String username, Long noteId){
        AuditLog log= new AuditLog();
        log.setAction("DELETE");
        log.setUsername(username);
        log.setNoteId(noteId);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    public void logNoteUpdate(String username, Note note){
        AuditLog log= new AuditLog();
        log.setAction("UPDATE");
        log.setUsername(username);
        log.setNoteId(note.getId());
        log.setNoteContent(note.getContent());
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }

    public List<AuditLog> allAuditLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getAllAuditLogsByNoteId(Long id) {
        return auditLogRepository.findByNoteId(id);
    }
}
