package com.shoppingmall.repository.impl;

import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.UserRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonUserRepository implements UserRepository {

    private static final String FILE_PATH = "data/users.json";
    private final Type listType = new TypeToken<List<User>>() {}.getType();

    @Override
    public void save(User user) {
        List<User> users = findAll();
        users.add(user);
        write(users);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            List<User> users = GsonProvider.get().fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void write(List<User> users) {
        new File("data").mkdirs();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GsonProvider.get().toJson(users, writer);
        } catch (IOException e) {
        }
    }
}
