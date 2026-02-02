package com.shoppingmall.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private String customerId;
    private List<CartItem> items;

    public Cart(String customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem newItem) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(newItem.getProduct().getId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void removeItem(String productId) {
        items.removeIf(item ->
                item.getProduct().getId().equals(productId));
    }

}
