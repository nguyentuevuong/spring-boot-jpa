package com.nittsu.kinjirou.identity.security.auth.jwt.extrator;

/**
 * Implementations of this interface should always return raw base-64 encoded
 * representation of JWT Token.
 */
public interface TokenExtractor {
    public String extract(String payload);
}
