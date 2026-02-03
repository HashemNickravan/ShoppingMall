package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class JsonUserRepository implements UserRepository {

    private static final String FILE_PATH = "data/users.json";

    private final Gson gson = new Gson();
    private Map<String, User> users;

    public JsonUserRepository() {
        users = load();
    }

    @Override
    public void save(User user) {
        users.put(user.getId(), user);
        persist();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Map<String, User> load() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            Type type = new TypeToken<Map<String, User>>() {}.getType();
            Map<String, User> data = gson.fromJson(new FileReader(file), type);

            return data != null ? data : new HashMap<>();

        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void persist() {
        try {
            new File("data").mkdirs();
            FileWriter writer = new FileWriter(FILE_PATH);
            gson.toJson(users, writer);
            writer.close();
        } catch (Exception ignored) {
        }
    }
}
