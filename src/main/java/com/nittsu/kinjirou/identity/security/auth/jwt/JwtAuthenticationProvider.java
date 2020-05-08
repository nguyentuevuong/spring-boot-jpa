package com.nittsu.kinjirou.identity.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import com.nittsu.kinjirou.identity.security.auth.JwtAuthenticationToken;
import com.nittsu.kinjirou.identity.security.configs.JwtSettings;
import com.nittsu.kinjirou.identity.security.model.UserAuthentication;
import com.nittsu.kinjirou.identity.security.model.token.RawAccessJwtToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * An {@link AuthenticationProvider} implementation that will use provided
 * instance of {@link JwtToken} to perform authentication.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private JwtSettings jwtSettings;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();

        String name = jwsClaims.getBody().get("name", String.class);

        @SuppressWarnings("unchecked")
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);

        List<GrantedAuthority> authorities = scopes.stream()
                // create new granted authority
                .map(SimpleGrantedAuthority::new)
                // to list
                .collect(Collectors.toList());

        UserAuthentication context = UserAuthentication.create(subject, name, authorities);

        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}