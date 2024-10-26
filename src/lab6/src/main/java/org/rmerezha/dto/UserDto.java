package org.rmerezha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record UserDto(int id, String name, String email, String password, @JsonProperty("role_id") int roleId) {}
