package com.shoppingmall.service;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.CartRepository;

import java.util.Optional;

public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public Cart getOrCreateCart(String username) {
        Optional<Cart> cartOpt = cartRepository.findByUsername(username);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        } else {
            Cart newCart = new Cart(username);
            cartRepository.save(newCart);
            return newCart;
        }
    }

    public void addToCart(String username, String productId, int quantity) {
        Cart cart = getOrCreateCart(username);
        cart.addItem(productId, quantity);
        cartRepository.save(cart);
    }

    public void updateCartItemQuantity(String username, String productId, int quantity) {
        Cart cart = getOrCreateCart(username);
        cart.updateQuantity(productId, quantity);
        cartRepository.save(cart);
    }

    public void removeFromCart(String username, String productId) {
        Cart cart = getOrCreateCart(username);
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cart.clear();
        cartRepository.save(cart);
    }

    public double calculateTotal(String username) {
        Cart cart = getOrCreateCart(username);
        double total = 0.0;

        for (String productId : cart.getItems().keySet()) {
            Optional<Product> productOpt = productService.getProductById(productId);
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                int quantity = cart.getItems().get(productId);
                total += product.getPrice() * quantity;
            }
        }

        return total;
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
