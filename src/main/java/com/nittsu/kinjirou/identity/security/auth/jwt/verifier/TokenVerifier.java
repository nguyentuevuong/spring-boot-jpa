package com.nittsu.kinjirou.identity.security.auth.jwt.verifier;

public interface TokenVerifier {
    public boolean verify(String jti);
}
