package org.rmerezha.dto;

import lombok.Builder;

@Builder
public record UserDto(int id, String name, String email, String password, int role_id) {}
