package com.shoppingmall.service;

import com.shoppingmall.model.Role;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(String username, String password, Role role, double initialBalance) {
        if (userRepository.existsByUsername(username)) {
            return false;
        }

        User user = new User(username, password, role, initialBalance);
        userRepository.save(user);
        return true;
    }

    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }

    public boolean isCustomer() {
        return currentUser != null && currentUser.getRole() == Role.CUSTOMER;
    }

    public void updateUserBalance(double newBalance) {
        if (currentUser != null) {
            currentUser.setBalance(newBalance);
            userRepository.save(currentUser);
        }
    }
}
