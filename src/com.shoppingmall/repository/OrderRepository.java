package com.shoppingmall.repository;

import com.shoppingmall.model.Order;
import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findByUsername(String username);
}
