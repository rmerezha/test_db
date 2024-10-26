package org.rmerezha.service;

import org.rmerezha.dto.UserDto;
import org.rmerezha.repository.UserRepository;
import org.rmerezha.util.Error;
import org.rmerezha.util.JsonBuilder;
import org.rmerezha.util.JsonParser;
import org.rmerezha.util.Status;

import java.io.InputStream;
import java.util.List;

public class UserService implements Service {

    private final JsonParser jsonParser = new JsonParser();
    private final UserRepository userRepository = new UserRepository();


    @Override
    public String get(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        var optUser = userRepository.get(userDto.id());
        if (optUser.isPresent()) {
            var user = optUser.get();
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", user.id())
                    .setData("name", user.name())
                    .setData("email", user.email())
                    .setData("password", user.password())
                    .setData("roleId", user.roleId())
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String add(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        try {
            var userDto = jsonParser.parse(jsonStream, UserDto.class);
            int id = userRepository.add(userDto);
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", id)
                    .build();
        } catch (Exception e) {
            jsonBuilder.setStatus(Status.FAIL).setErrors(List.of(Error.USER_EXIST))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String update(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        boolean isUpdated = userRepository.update(userDto);
        if (isUpdated) {
            jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String remove(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var userDto = jsonParser.parse(jsonStream, UserDto.class);
        boolean isRemoved = userRepository.remove(userDto.id());
        if (isRemoved) {
            jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.USER_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }
}
