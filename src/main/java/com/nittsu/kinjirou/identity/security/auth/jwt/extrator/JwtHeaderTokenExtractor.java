package com.nittsu.kinjirou.identity.security.auth.jwt.extrator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

/**
 * An implementation of {@link TokenExtractor} extracts token
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    private static final String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("authorization_header_cannot_be_blank");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("invalid_authorization_header_size");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
