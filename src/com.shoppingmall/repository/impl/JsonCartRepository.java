package com.shoppingmall.repository.impl;

import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Cart;
import com.shoppingmall.repository.CartRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonCartRepository implements CartRepository {

    private static final String FILE_PATH = "data/carts.json";
    private final Type listType = new TypeToken<List<Cart>>(){}.getType();

    @Override
    public void save(Cart cart) {
        List<Cart> carts = findAll();
        carts.removeIf(c -> c.getCustomerId().equals(cart.getCustomerId()));
        carts.add(cart);
        write(carts);
    }

    @Override
    public Optional<Cart> findByCustomerId(String customerId) {
        return findAll().stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst();
    }

    private List<Cart> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            return GsonProvider.get().fromJson(reader, listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(List<Cart> carts) {
        new File("data").mkdirs();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GsonProvider.get().toJson(carts, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
