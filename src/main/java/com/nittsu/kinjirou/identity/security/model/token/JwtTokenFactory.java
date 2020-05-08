package com.nittsu.kinjirou.identity.security.model.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.nittsu.kinjirou.identity.entity.Role;
import com.nittsu.kinjirou.identity.security.configs.JwtSettings;
import com.nittsu.kinjirou.identity.security.model.UserAuthentication;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Factory class that should be always used to create {@link JwtToken}.
 */
@Component
public class JwtTokenFactory {
    @Autowired
    private JwtSettings settings;

    /**
     * Create access token
     * @param userContext
     * @return
     */
    public String createAccessJwtToken(UserAuthentication userContext) {
        return createJwtToken(userContext, true);
    }

    /**
     * Create refresh token
     * @param userContext
     * @return
     */
    public String createRefreshJwtToken(UserAuthentication userContext) {
        return createJwtToken(userContext, false);
    }

    /**
     * Bussiness logic generate jwt
     * @param userContext
     * @param isAccess
     * @return
     */
    private String createJwtToken(UserAuthentication userContext, boolean isAccess) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("cannot_create_jwt_token_without_username");
        }

        if (isAccess && CollectionUtils.isEmpty(userContext.getAuthorities())) {
            throw new IllegalArgumentException("user_doesn_t_have_any_privileges");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusMinutes(settings.getRefreshTokenExpTime());

        Map<String, Object> header = new HashMap<>();
        header.put(Header.TYPE, Header.JWT_TYPE);

        Claims claims = Jwts.claims();
        String subject = userContext.getUsername();
        String displayName = userContext.getDisplayName();
            
        List<String> refreshAuthorities = Arrays.asList(Role.REFRESH_TOKEN.authority());
        List<String> accessAuthorities = userContext.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList());
        
        // username or identity
        claims.setSubject(subject);

        // add any attribute at here
        claims.put("name", displayName);

        // authorities
        claims.put("scopes", isAccess ? accessAuthorities : refreshAuthorities);
        
        return Jwts.builder()
            .setHeader(header)
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuer(settings.getTokenIssuer())
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
            .compact();
    }
}
