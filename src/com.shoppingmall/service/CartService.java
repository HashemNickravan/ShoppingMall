package com.shoppingmall.service;

import com.shoppingmall.model.Cart;
import com.shoppingmall.repository.CartRepository;

public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart(userId);
                    cartRepository.save(cart);
                    return cart;
                });
    }

    public void addToCart(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.addItem(productId, quantity);
        cartRepository.save(cart);
    }

    public void updateQuantity(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        if (quantity <= 0) {
            cart.removeItem(productId);
        } else {
            cart.setQuantity(productId, quantity);
        }
        cartRepository.save(cart);
    }

    public void removeFromCart(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
