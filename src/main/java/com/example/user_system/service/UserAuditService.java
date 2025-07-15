package com.example.user_system.service;

import com.example.user_system.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuditService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Number> getRevisions(Long userId) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        return reader.getRevisions(User.class, userId);
    }

    public User getUserAtRevision(Long userId, Number revision) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        return reader.find(User.class, userId, revision);
    }

    @Transactional
    public void rollbackToRevision(Long userId, Number revision) {
        AuditReader reader = AuditReaderFactory.get(entityManager);
        User oldUser = reader.find(User.class, userId, revision);

        if (oldUser != null) {
            // Здесь можно сохранить старую версию как текущую
            // В реальности надо использовать репозиторий UserRepository для сохранения
            entityManager.merge(oldUser);
        }
    }
}