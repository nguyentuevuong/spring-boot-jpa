package com.nittsu.kinjirou.identity.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Raw representation of JWT Token.
 */
@Getter
@AllArgsConstructor
public final class AccessJwtToken implements JwtToken {
    private final String token;

    @JsonIgnore 
    private final Claims claims;
}
