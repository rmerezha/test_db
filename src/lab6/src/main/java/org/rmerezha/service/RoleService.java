package org.rmerezha.service;

import org.rmerezha.dto.RoleDto;
import org.rmerezha.dto.UserDto;
import org.rmerezha.repository.RoleRepository;
import org.rmerezha.repository.UserRepository;
import org.rmerezha.util.Error;
import org.rmerezha.util.JsonBuilder;
import org.rmerezha.util.JsonParser;
import org.rmerezha.util.Status;

import java.io.InputStream;
import java.util.List;

public class RoleService implements Service {

    private final JsonParser jsonParser = new JsonParser();
    private final RoleRepository roleRepository = new RoleRepository();

    @Override
    public String get(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        var optRole = roleRepository.get(roleDto.id());
        if (optRole.isPresent()) {
            var role = optRole.get();
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", role.id())
                    .setData("name", role.name())
                    .setData("description", role.description())
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String add(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        try {
            var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
            int id = roleRepository.add(roleDto);
            jsonBuilder.setStatus(Status.SUCCESS)
                    .setData("id", id)
                    .build();
        } catch (Exception e) {
            jsonBuilder.setStatus(Status.FAIL).setErrors(List.of(Error.ROLE_EXIST))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String update(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        boolean isUpdated = roleRepository.update(roleDto);
        if (isUpdated) {
            jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }

    @Override
    public String remove(InputStream jsonStream) {
        JsonBuilder jsonBuilder = new JsonBuilder();
        var roleDto = jsonParser.parse(jsonStream, RoleDto.class);
        boolean isRemoved = roleRepository.remove(roleDto.id());
        if (isRemoved) {
            jsonBuilder.setStatus(Status.SUCCESS)
                    .build();
        } else {
            jsonBuilder.setStatus(Status.FAIL)
                    .setErrors(List.of(Error.ROLE_NOT_FOUND))
                    .build();
        }
        return jsonBuilder.toString();
    }
}
