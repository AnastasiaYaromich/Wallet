package com.yaromich.wallet.mappers;

import com.yaromich.wallet.domain.dto.RoleDto;
import com.yaromich.wallet.domain.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper MAPPER = Mappers.getMapper(RoleMapper.class);

    RoleDto roleToRoleDto(Role role);
    Role roleDtoToURole(RoleDto roleDto);
}
