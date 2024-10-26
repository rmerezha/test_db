package org.rmerezha.entity;

import lombok.Builder;

@Builder
public record Role(int id, String name, String description) {
}
