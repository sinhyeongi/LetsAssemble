package com.la.letsassemble.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UsersRole {
    ROLE_USER("user"),ROLE_MANAGER("manager"),ROLE_ADMIN("admin");
    private final String role;
}
