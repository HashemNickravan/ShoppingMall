package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class JsonProductRepository implements ProductRepository {
    private static final String FILE_PATH = "data/products.json";
    private final Gson gson;
    private Map<String, Product> products;

    public JsonProductRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.products = new HashMap<>();
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
            Type type = new TypeToken<Map<String, Product>>(){}.getType();
            Map<String, Product> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                products = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(products, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
        saveToFile();
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void deleteById(String id) {
        products.remove(id);
        saveToFile();
    }
}
