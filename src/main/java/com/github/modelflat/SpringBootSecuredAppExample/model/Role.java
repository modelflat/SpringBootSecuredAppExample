package com.github.modelflat.SpringBootSecuredAppExample.model;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {

    private String name;

    private Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    // could easily be moved to mongo
    public static final Role USER = new Role("ROLE_USER");
    public static final Role ADMIN = new Role("ROLE_ADMIN");

}
