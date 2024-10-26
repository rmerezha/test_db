package org.rmerezha.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Role{
    int id;
    String name;
    String description;
}
