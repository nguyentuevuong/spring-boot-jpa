package com.nittsu.kinjirou.identity.security.auth.jwt.verifier;

import org.springframework.stereotype.Component;

/**
 * BloomFilterTokenVerifier
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
