package org.rmerezha.mapper;

import org.rmerezha.dto.UserDto;
import org.rmerezha.entity.User;

public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getRoleId()
        );
    }

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.password(),
                userDto.roleId()
        );
    }

}
