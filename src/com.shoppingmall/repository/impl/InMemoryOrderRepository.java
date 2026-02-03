package com.shoppingmall.repository.impl;

import com.shoppingmall.model.Order;
import com.shoppingmall.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOrderRepository implements OrderRepository {

    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
    }

    @Override
    public List<Order> findByUsername(String username) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUsername().equals(username)) {
                result.add(o);
            }
        }
        return result;
    }
}
