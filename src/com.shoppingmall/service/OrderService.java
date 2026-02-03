package com.shoppingmall.service;

import com.shoppingmall.model.Order;
import com.shoppingmall.model.OrderItem;
import com.shoppingmall.repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepository;
    private int nextId = 1;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String username, List<OrderItem> items) {
        Order order = new Order(nextId++, username, items);
        orderRepository.save(order);
        return order;
    }

    public List<Order> getOrdersByUser(String username) {
        return orderRepository.findByUsername(username);
    }
}
