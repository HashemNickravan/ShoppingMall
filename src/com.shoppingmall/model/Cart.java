package com.shoppingmall.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private String username;
    private Map<String, Integer> items; // productId -> quantity

    public Cart() {
        this.items = new HashMap<>();
    }

    public Cart(String username) {
        this.username = username;
        this.items = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void updateQuantity(String productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
        } else {
            items.put(productId, quantity);
        }
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
