package com.shoppingmall.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String username;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String orderId, String username, List<OrderItem> items, double totalAmount, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.username = username;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
