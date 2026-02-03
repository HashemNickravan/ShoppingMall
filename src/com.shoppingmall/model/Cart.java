package com.shoppingmall.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private String userId;
    private Map<Product, Integer> items = new HashMap<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void setQuantity(Product product, int quantity) {
        items.put(product, quantity);
    }

    public void removeItem(Product product) {
        items.remove(product);
    }

    public void clear() {
        items.clear();
    }
}
