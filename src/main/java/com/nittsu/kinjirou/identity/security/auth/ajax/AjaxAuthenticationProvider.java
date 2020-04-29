package com.nittsu.kinjirou.identity.security.auth.ajax;

import java.util.List;
import java.util.stream.Collectors;

import com.nittsu.kinjirou.identity.entity.User;
import com.nittsu.kinjirou.identity.security.UserService;
import com.nittsu.kinjirou.identity.security.model.UserContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    /**
     * Method for /auth/login
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        User user = userService.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Authentication Failed. Username or Password not valid.");
        }

        if (user.getRoles() == null) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                // authority -> string
                .map(authority -> authority.getRole().authority())
                // create new granted authority
                .map(SimpleGrantedAuthority::new)
                // to list
                .collect(Collectors.toList());

        UserContext userContext = UserContext.create(user.getUsername(), user.getDisplayName(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
