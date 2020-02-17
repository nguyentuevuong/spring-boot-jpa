package com.nittsu.kinjirou.update.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String jwt;
}
