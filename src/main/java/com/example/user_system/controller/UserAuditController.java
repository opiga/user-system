package com.example.user_system.controller;

import com.example.user_system.entity.User;
import com.example.user_system.service.UserAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/users/audit")
@RequiredArgsConstructor
public class UserAuditController {

    private final UserAuditService auditService;

    @GetMapping("/{id}/revisions")
    public List<Number> getRevisions(@PathVariable Long id) {
        return auditService.getRevisions(id);
    }

    @GetMapping("/{id}/revisions/{rev}")
    public User getUserAtRevision(@PathVariable Long id, @PathVariable Number rev) {
        return auditService.getUserAtRevision(id, rev);
    }

    @PostMapping("/{id}/rollback/{rev}")
    public void rollback(@PathVariable Long id, @PathVariable Number rev) {
        auditService.rollbackToRevision(id, rev);
    }
}