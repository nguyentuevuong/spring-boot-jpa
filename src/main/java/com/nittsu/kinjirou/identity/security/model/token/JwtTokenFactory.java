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
     * Factory method for issuing new JWT Tokens.
     * 
     * @param username
     * @param roles
     * @return
     */
    public String createAccessJwtToken(UserAuthentication userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        if (CollectionUtils.isEmpty(userContext.getAuthorities())) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Map<String, Object> header = new HashMap<>();
        header.put(Header.TYPE, Header.JWT_TYPE);

        Claims claims = Jwts.claims();
        String subject = userContext.getUsername();
        String displayName = userContext.getDisplayName();
        List<String> authorities = userContext.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList());
        
        // username or identity
        claims.setSubject(subject);

        // add any attribute at here
        claims.put("name", displayName);

        // authorities
        claims.put("scopes", authorities);
        
        return Jwts.builder()
            .setHeader(header)
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuer(settings.getTokenIssuer())
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(currentTime
                .plusMinutes(settings.getTokenExpirationTime())
                .atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();
    }

    public String createRefreshJwtToken(UserAuthentication userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT Token without username");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Map<String, Object> header = new HashMap<>();
        header.put(Header.TYPE, Header.JWT_TYPE);

        Claims claims = Jwts.claims();
        String subject = userContext.getUsername();
        String displayName = userContext.getDisplayName();

        // username or identity
        claims.setSubject(subject);

        // add any attribute at here
        claims.put("name", displayName);

        claims.put("scopes", Arrays.asList(Role.REFRESH_TOKEN.authority()));

        return Jwts.builder()
            .setHeader(header)
            .setClaims(claims)
            .setId(UUID.randomUUID().toString())
            .setIssuer(settings.getTokenIssuer())
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(Date.from(currentTime
                .plusMinutes(settings.getRefreshTokenExpTime())
                .atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
    }
}
