package com.shoppingmall.model;

import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private int id;
    private String username;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    public Order(int id, String username, List<OrderItem> items) {
        this.id = id;
        this.username = username;
        this.items = items;
        this.createdAt = LocalDateTime.now();
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
