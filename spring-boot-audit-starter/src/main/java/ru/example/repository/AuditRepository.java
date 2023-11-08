package ru.example.repository;

import org.springframework.stereotype.Repository;
import ru.example.model.Audit;

@Repository
public interface AuditRepository {
     void save(Audit audit);
}
