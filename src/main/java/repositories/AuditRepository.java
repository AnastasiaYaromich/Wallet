package repositories;

import domain.models.Audit;
import java.util.List;

public interface AuditRepository {
    Audit save(String login, Audit audit);
    List<Audit> findAll(String login);
}
