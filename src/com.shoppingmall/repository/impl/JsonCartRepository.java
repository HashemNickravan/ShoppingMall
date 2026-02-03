package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Cart;
import com.shoppingmall.repository.CartRepository;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonCartRepository implements CartRepository {

    private static final Path FILE_PATH = Path.of("data", "carts.json");
    private final Gson gson = new Gson();
    private Map<String, Cart> carts = new HashMap<>();

    public JsonCartRepository() {
        load();
    }

    private void load() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            if (!Files.exists(FILE_PATH)) {
                saveToFile();
                return;
            }
            String json = Files.readString(FILE_PATH);
            Type type = new TypeToken<Map<String, Cart>>() {}.getType();
            Map<String, Cart> data = gson.fromJson(json, type);
            if (data != null) {
                carts = data;
            }
        } catch (Exception e) {
            carts = new HashMap<>();
        }
    }

    private void saveToFile() {
        try {
            Files.writeString(FILE_PATH, gson.toJson(carts));
        } catch (Exception ignored) {
        }
    }

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return Optional.ofNullable(carts.get(userId));
    }

    @Override
    public void save(Cart cart) {
        carts.put(cart.getUserId(), cart);
        saveToFile();
    }

    @Override
    public void deleteByUserId(String userId) {
        carts.remove(userId);
        saveToFile();
    }
}
