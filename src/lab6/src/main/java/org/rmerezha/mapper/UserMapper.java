package org.rmerezha.mapper;

import org.rmerezha.dto.UserDto;
import org.rmerezha.entity.User;

public class UserMapper implements Mapper<UserDto, User> {

    @Override
    public UserDto toDto(User user) {
        return new UserDto(
                user.id(),
                user.name(),
                user.email(),
                user.password(),
                user.role_id()
        );
    }

    @Override
    public User toEntity(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email(),
                userDto.password(),
                userDto.role_id()
        );
    }

}
