package com.shoppingmall.repository;

import com.shoppingmall.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(String orderId);
    List<Order> findByUsername(String username);
    List<Order> findAll();
}
