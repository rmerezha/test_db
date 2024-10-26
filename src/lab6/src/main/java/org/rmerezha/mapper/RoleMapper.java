package org.rmerezha.mapper;

import org.rmerezha.dto.RoleDto;
import org.rmerezha.entity.Role;

public class RoleMapper implements Mapper<RoleDto, Role> {

    @Override
    public RoleDto toDto(Role role) {
        return new RoleDto(
                role.id(),
                role.name(),
                role.description()
        );
    }

    @Override
    public Role toEntity(RoleDto roleDto) {
        return new Role(
                roleDto.id(),
                roleDto.name(),
                roleDto.description()
        );
    }
}
