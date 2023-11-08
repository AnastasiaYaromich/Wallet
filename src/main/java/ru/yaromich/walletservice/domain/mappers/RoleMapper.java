package ru.yaromich.walletservice.domain.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.yaromich.walletservice.domain.dtos.RoleDto;
import ru.yaromich.walletservice.domain.models.Role;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper {
    RoleMapper MAPPER = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);
    Role roleDtoToURole(RoleDto roleDto);
}
