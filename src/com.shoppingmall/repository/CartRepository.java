package com.shoppingmall.repository;

import com.shoppingmall.model.Cart;
import java.util.Optional;

public interface CartRepository {
    void save(Cart cart);
    Optional<Cart> findByUsername(String username);
    void deleteByUsername(String username);
}
