package com.shoppingmall.repository.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class JsonProductRepository implements ProductRepository {

    private static final String FILE_PATH = "data/products.json";

    private final Gson gson = new Gson();
    private Map<String, Product> products;

    public JsonProductRepository() {
        products = load();
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
        persist();
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
        persist();
    }

    private Map<String, Product> load() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists() || file.length() == 0) {
                return new HashMap<>();
            }

            Type type = new TypeToken<Map<String, Product>>() {}.getType();
            Map<String, Product> data = gson.fromJson(new FileReader(file), type);

            return data != null ? data : new HashMap<>();

        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private void persist() {
        try {
            new File("data").mkdirs();
            FileWriter writer = new FileWriter(FILE_PATH);
            gson.toJson(products, writer);
            writer.close();
        } catch (Exception ignored) {
        }
    }
}
