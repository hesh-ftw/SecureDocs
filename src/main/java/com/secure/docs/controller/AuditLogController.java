package com.secure.docs.controller;

import com.secure.docs.model.AuditLog;
import com.secure.docs.repository.AuditLogRepository;
import com.secure.docs.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/audit")
public class AuditLogController {
    @Autowired
    AuditLogService auditLogService;


    @GetMapping
    public List<AuditLog> getAllAuditLogs(){
        return auditLogService.allAuditLogs();
    }

    @GetMapping("/note/{id}")
    public List<AuditLog> getAuditsByNoteId(@PathVariable Long id){
        return auditLogService.getAllAuditLogsByNoteId(id);
    }
}
