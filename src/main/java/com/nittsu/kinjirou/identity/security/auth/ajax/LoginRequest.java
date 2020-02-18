package com.nittsu.kinjirou.identity.security.auth.ajax;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * Model intended to be used for AJAX based authentication.
 */

 @Getter
public class LoginRequest {
    private String username;
    private String password;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        this.username = username;
        this.password = password;
    }
}
