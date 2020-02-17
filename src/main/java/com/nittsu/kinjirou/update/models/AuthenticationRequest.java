package com.nittsu.kinjirou.update.models;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
}
