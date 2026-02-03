package com.shoppingmall.service;

import com.shoppingmall.model.Order;
import com.shoppingmall.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private int nextId = 1;

    public Order createOrder(String username, List<OrderItem> items) {
        Order order = new Order(nextId++, username, items);
        orders.add(order);
        return order;
    }

    public List<Order> getOrdersByUser(String username) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUsername().equals(username)) {
                result.add(o);
            }
        }
        return result;
    }
}
