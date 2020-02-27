package com.nittsu.kinjirou.update.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * End-point for retrieving logged-in user details.
 */
@RestController
@RequestMapping({ "/sample" })
public class SampleController {
    @Secured({ "ROLE_ADMIN" })
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping({ "/string" })
    public String getSampleString() {
        return "Hello world!";
    }
}