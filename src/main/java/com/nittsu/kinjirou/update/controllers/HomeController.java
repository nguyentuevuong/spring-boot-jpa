package com.nittsu.kinjirou.update.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";
    public static final String SECURED_TEXT = "Hello from the secured resource!";

    @RequestMapping(path = "/hello")
    public String sayHello() {
        return HELLO_TEXT;
    }
}