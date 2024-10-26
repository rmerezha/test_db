package org.rmerezha.entity;

import lombok.Builder;

@Builder
public record User(int id, String name, String email, String password, int role_id) {}
