package com.shoppingmall.repository.impl;

import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
