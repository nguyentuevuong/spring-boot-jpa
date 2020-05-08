package com.nittsu.kinjirou.identity.security.model;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class UserAuthentication {
    private final String username;
    private final String displayName;

    private final List<GrantedAuthority> authorities;

    private UserAuthentication(final String username, final String displayName, final List<GrantedAuthority> authorities) {
        this.username = username;
        this.displayName = displayName;
        this.authorities = authorities;
    }

    public static UserAuthentication create(String username, List<GrantedAuthority> authorities) {
        return UserAuthentication.create(username, "", authorities);
    }

    public static UserAuthentication create(String username, String displayName, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("Username is blank: " + username);
        }

        return new UserAuthentication(username, displayName, authorities);
    }
}
