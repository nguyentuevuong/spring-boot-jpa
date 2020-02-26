package com.nittsu.kinjirou.identity.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nittsu.kinjirou.core.config.WebSecurityConfig;
import com.nittsu.kinjirou.identity.entity.User;
import com.nittsu.kinjirou.identity.security.UserService;
import com.nittsu.kinjirou.identity.security.auth.jwt.extrator.TokenExtractor;
import com.nittsu.kinjirou.identity.security.auth.jwt.verifier.TokenVerifier;
import com.nittsu.kinjirou.identity.security.configs.JwtSettings;
import com.nittsu.kinjirou.identity.security.exceptions.InvalidJwtToken;
import com.nittsu.kinjirou.identity.security.model.UserContext;
import com.nittsu.kinjirou.identity.security.model.token.JwtToken;
import com.nittsu.kinjirou.identity.security.model.token.JwtTokenFactory;
import com.nittsu.kinjirou.identity.security.model.token.RawAccessJwtToken;
import com.nittsu.kinjirou.identity.security.model.token.RefreshToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/auth" })
public class RefreshTokenController {

    @Autowired
    private JwtSettings jwtSettings;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenVerifier tokenVerifier;
    
    @Autowired
    private JwtTokenFactory tokenFactory;

    @Autowired
    private TokenExtractor tokenExtractor;

    @ResponseBody
    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.AUTHENTICATION_HEADER_NAME));

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
        RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey())
                .orElseThrow(() -> new InvalidJwtToken());

        String jti = refreshToken.getJti();

        if (!tokenVerifier.verify(jti)) {
            throw new InvalidJwtToken();
        }

        String subject = refreshToken.getSubject();
        User user = userService.getByUsername(subject)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getRoles() == null) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
                .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUsername(), user.getDisplayName(), authorities);

        return tokenFactory.createAccessJwtToken(userContext);
    }
}
