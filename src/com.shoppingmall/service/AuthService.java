package com.shoppingmall.service;

import com.shoppingmall.model.Role;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

public class AuthService {

    private final UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password))
            throw new RuntimeException("Wrong password");

        currentUser = user;
        return user;
    }

    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent())
            throw new RuntimeException("Username already exists");

        User user = new User(
                java.util.UUID.randomUUID().toString(),
                username,
                password,
                Role.CUSTOMER,
                0
        );

        userRepository.save(user);
        currentUser = user;
        return user;
    }

    public void logout() {
        currentUser = null;
    }
}
