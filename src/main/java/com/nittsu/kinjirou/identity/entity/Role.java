package com.nittsu.kinjirou.identity.entity;

public enum Role {
    ADMIN,

    PREMIUM_MEMBER,

    MEMBER,
    
    REFRESH_TOKEN;

    public String authority() {
        return "ROLE_" + this.name();
    }
}
