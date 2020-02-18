package com.nittsu.kinjirou.identity.security;

import java.util.Optional;
import com.nittsu.kinjirou.identity.entity.User;

public interface UserService {
    public Optional<User> getByUsername(String username);
}