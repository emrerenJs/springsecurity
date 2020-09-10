package com.emrerenjs.springsecurity.Entities;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class Role implements Serializable, GrantedAuthority {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.getRole();
    }
}
