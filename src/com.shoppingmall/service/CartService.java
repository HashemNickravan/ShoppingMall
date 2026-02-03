package com.shoppingmall.service;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.Product;
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

    public void addToCart(String userId, Product product, int quantity) {
        Cart cart = getCartByUserId(userId);
        cart.addItem(product.getId(), quantity);
        cartRepository.save(cart);
    }

    public void updateQuantity(String userId, Product product, int quantity) {
        Cart cart = getCartByUserId(userId);
        if (quantity <= 0) {
            cart.removeItem(product.getId());
        } else {
            cart.setQuantity(product.getId(), quantity);
        }
        cartRepository.save(cart);
    }

    public void removeFromCart(String userId, Product product) {
        Cart cart = getCartByUserId(userId);
        cart.removeItem(product.getId());
        cartRepository.save(cart);
    }

    public void clearCart(String userId) {
        cartRepository.deleteByUserId(userId);
    }
}
