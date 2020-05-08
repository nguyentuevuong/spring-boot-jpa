package com.nittsu.kinjirou.identity.controllers;

import com.nittsu.kinjirou.identity.security.auth.JwtAuthenticationToken;
import com.nittsu.kinjirou.identity.security.model.UserAuthentication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * End-point for retrieving logged-in user details.
 */
@RestController
@RequestMapping({ "/profile" })
public class ProfileController {
    @ResponseBody
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public UserAuthentication get(JwtAuthenticationToken token) {
        return (UserAuthentication) token.getPrincipal();
    }
}