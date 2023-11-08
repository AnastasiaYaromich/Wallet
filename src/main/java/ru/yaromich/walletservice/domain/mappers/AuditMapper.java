package ru.yaromich.walletservice.domain.mappers;


import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.yaromich.walletservice.domain.dtos.AuditDto;
import ru.yaromich.walletservice.domain.models.Audit;

import java.util.Optional;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuditMapper {
    AuditMapper MAPPER = Mappers.getMapper(AuditMapper.class);

    AuditDto auditToAuditDto(Audit audit);
    Audit auditDtoToAudit(AuditDto auditDto);

    @AfterMapping
    default void setAudit(@MappingTarget Audit audit) {
        Optional.ofNullable(audit.getUser())
                .ifPresent(user -> user.getAudits().add(audit));
    }

    @AfterMapping
    default void setAuditDto(@MappingTarget AuditDto auditDto) {
        Optional.ofNullable(auditDto.getUserDto())
                .ifPresent(userDto -> userDto.getAudits().add(auditDto));
    }
}
