package org.rmerezha.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class User{
    int id;
    String name; String email;
    String password;
    int roleId;
}
