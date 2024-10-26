package org.rmerezha.util;

import lombok.Getter;

@Getter
public enum Status {
    SUCCESS("success"),
    FAIL("fail");

    private final String type;

    Status(String type) {
        this.type = type;
    }

}