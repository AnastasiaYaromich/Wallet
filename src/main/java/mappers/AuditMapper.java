package mappers;

import domain.models.Audit;
import dto.AuditDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuditMapper {
    AuditMapper MAPPER = Mappers.getMapper(AuditMapper.class);

    AuditDto mapToAuditDto(Audit audit);
    Audit mapToAudit(AuditDto auditDto);
}
