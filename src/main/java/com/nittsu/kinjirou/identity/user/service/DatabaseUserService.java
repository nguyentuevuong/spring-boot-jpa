package com.nittsu.kinjirou.identity.user.service;

import java.util.Optional;

import com.nittsu.kinjirou.identity.entity.User;
import com.nittsu.kinjirou.identity.security.UserService;
import com.nittsu.kinjirou.identity.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mock implementation.
 */
@Service
public class DatabaseUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}
