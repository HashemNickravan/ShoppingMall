package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Cart;
import com.shoppingmall.repository.CartRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonCartRepository implements CartRepository {
    private static final String FILE_PATH = "data/carts.json";
    private final Gson gson;
    private Map<String, Cart> carts;

    public JsonCartRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.carts = new HashMap<>();
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
            Type type = new TypeToken<Map<String, Cart>>(){}.getType();
            Map<String, Cart> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                carts = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(carts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Cart cart) {
        carts.put(cart.getUsername(), cart);
        saveToFile();
    }

    @Override
    public Optional<Cart> findByUsername(String username) {
        return Optional.ofNullable(carts.get(username));
    }

    @Override
    public void deleteByUsername(String username) {
        carts.remove(username);
        saveToFile();
    }
}
