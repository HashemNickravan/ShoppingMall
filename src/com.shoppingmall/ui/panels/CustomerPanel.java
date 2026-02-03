package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    public CustomerPanel(User user,
                         ProductService productService,
                         CartService cartService,
                         ProductRepository productRepository,
                         AuthService authService) {

        setLayout(new BorderLayout());

        JLabel header = new JLabel(
                "Customer Dashboard - " + user.getUsername(),
                SwingConstants.CENTER
        );
        add(header, BorderLayout.NORTH);

        ProductListPanel productListPanel =
                new ProductListPanel(
                        productService,
                        cartService,
                        user
                );
        add(productListPanel, BorderLayout.CENTER);

        CartPanel cartPanel =
                new CartPanel(
                        cartService,
                        productRepository,
                        authService,
                        user.getId()
                );
        add(cartPanel, BorderLayout.SOUTH);
    }
}
