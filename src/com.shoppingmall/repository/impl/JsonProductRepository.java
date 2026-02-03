package com.shoppingmall.repository.impl;

import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonProductRepository implements ProductRepository {

    private static final String FILE_PATH = "data/products.json";
    private final Type listType = new TypeToken<List<Product>>() {}.getType();

    @Override
    public void save(Product product) {
        List<Product> products = findAll();
        products.add(product);
        write(products);
    }

    @Override
    public List<Product> findAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            List<Product> products = GsonProvider.get().fromJson(reader, listType);
            return products != null ? products : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Product> findById(String id) {
        return findAll().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    private void write(List<Product> products) {
        new File("data").mkdirs();
        try (Writer writer = new FileWriter(FILE_PATH)) {
            GsonProvider.get().toJson(products, writer);
        } catch (IOException e) {
        }
    }
}
