package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonUserRepository implements UserRepository {
    private static final String FILE_PATH = "data/users.json";
    private final Gson gson;
    private Map<String, User> users;

    public JsonUserRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveToFile();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, User>>(){}.getType();
            Map<String, User> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                users = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
        saveToFile();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }
}
