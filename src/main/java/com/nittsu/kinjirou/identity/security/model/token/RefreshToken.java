package com.nittsu.kinjirou.identity.security.model.token;

import java.util.List;
import java.util.Optional;

import com.nittsu.kinjirou.identity.security.model.Scopes;

import org.springframework.security.authentication.BadCredentialsException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.Getter;

import org.apache.commons.collections4.CollectionUtils;

/**
 * RefreshToken
 */
public class RefreshToken {
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

        if (CollectionUtils.isEmpty(scopes) || !scopes.stream()
                .filter(scope -> Scopes.REFRESH_TOKEN.authority().equals(scope)).findFirst().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(claims));
    }

    public String getJti() {
        return claims.getBody().getId();
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
