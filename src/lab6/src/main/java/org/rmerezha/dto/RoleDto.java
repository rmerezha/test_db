package org.rmerezha.dto;

import lombok.Builder;

@Builder
public record RoleDto(int id, String name, String description) {}