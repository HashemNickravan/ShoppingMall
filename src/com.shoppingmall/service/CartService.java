package com.shoppingmall.service;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.CartItem;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.CartRepository;

public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByCustomerId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart(userId);
                    cartRepository.save(cart);
                    return cart;
                });
    }

    public void addToCart(Cart cart, Product product, int quantity) {
        cart.addItem(new CartItem(product, quantity));
        cartRepository.save(cart);
    }

    public void removeFromCart(Cart cart, String productId) {
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public double calculateTotalPrice(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
