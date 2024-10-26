package org.rmerezha.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {
    USER_EXIST(1, "User with this email already exists"),
    USER_NOT_FOUND(3, "User with this id does not exist"),
    ROLE_EXIST(1, "Role with this name already exists"),
    ROLE_NOT_FOUND(3, "Role with this id does not exist");

    private final int code;
    private final String message;

}
