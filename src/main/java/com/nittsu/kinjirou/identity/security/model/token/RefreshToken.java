package com.nittsu.kinjirou.identity.security.model.token;

import java.util.List;
import java.util.Optional;

import com.nittsu.kinjirou.identity.security.model.Scopes;

import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

/**
 * RefreshToken
 */
public class RefreshToken implements JwtToken {
    @Getter
    private Jws<Claims> claims;

    private RefreshToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    /**
     * Creates and validates Refresh token 
     * 
     * @param token
     * @param signingKey
     * 
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     * 
     * @return
     */
    public static Optional<RefreshToken> create(RawAccessJwtToken token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        @SuppressWarnings("unchecked")
        List<String> scopes = claims.getBody().get("scopes", List.class);

        if (scopes == null || scopes.isEmpty() 
                || !scopes.stream().filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }

    @Override
    public String getToken() {
        return null;
    }
    
    public String getJti() {
        return claims.getBody().getId();
    }
    
    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
