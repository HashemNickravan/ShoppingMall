package com.shoppingmall.repository.impl;

import com.shoppingmall.model.Cart;
import com.shoppingmall.repository.CartRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonCartRepository implements CartRepository {

    private final Map<String, Cart> carts = new HashMap<>();

    @Override
    public Optional<Cart> findByUserId(String userId) {
        return Optional.ofNullable(carts.get(userId));
    }

    @Override
    public void save(Cart cart) {
        carts.put(cart.getUserId(), cart);
    }
}
