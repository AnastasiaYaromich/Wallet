package com.yaromich.wallet.mappers;

import com.yaromich.wallet.domain.dto.AuditDto;
import com.yaromich.wallet.domain.model.Audit;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper
public interface AuditMapper {
    AuditMapper MAPPER = Mappers.getMapper(AuditMapper.class);

    AuditDto auditToAuditDto(Audit audit);
    Audit auditDtoToAudit(AuditDto auditDto);

    @AfterMapping
    default void setAudit(@MappingTarget Audit audit) {
        Optional.ofNullable(audit.getUser())
                .ifPresent(user -> user.getAudits().add(audit));
    }
}
