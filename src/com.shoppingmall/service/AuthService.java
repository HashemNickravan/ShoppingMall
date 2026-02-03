package com.shoppingmall.service;

import com.shoppingmall.model.Role;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        User user = new User(
                UUID.randomUUID().toString(),
                username,
                password,
                Role.CUSTOMER,
                0
        );

        userRepository.save(user);
        currentUser = user;
        return user;
    }

    public User login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalStateException("Invalid credentials");
        }

        User user = userOpt.get();
        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("Invalid credentials");
        }

        currentUser = user;
        return user;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
