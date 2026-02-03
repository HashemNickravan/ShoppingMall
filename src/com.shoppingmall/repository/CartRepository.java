package com.shoppingmall.repository;

import com.shoppingmall.model.Cart;
import java.util.Optional;

public interface CartRepository {

    Optional<Cart> findByUserId(String userId);

    void save(Cart cart);

    void deleteByUserId(String userId);
}
