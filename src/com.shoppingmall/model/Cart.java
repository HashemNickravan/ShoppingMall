package com.shoppingmall.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private String userId;
    private Map<String, Integer> items = new HashMap<>();

    public Cart(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void addItem(String productId, int quantity) {
        items.put(productId, items.getOrDefault(productId, 0) + quantity);
    }

    public void setQuantity(String productId, int quantity) {
        items.put(productId, quantity);
    }

    public void removeItem(String productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }
}
