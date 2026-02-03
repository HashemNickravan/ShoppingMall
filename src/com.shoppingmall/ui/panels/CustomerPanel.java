package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.OrderService;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    public CustomerPanel(User user,
                         ProductService productService,
                         CartService cartService,
                         ProductRepository productRepository,
                         AuthService authService,
                         OrderService orderService) {

        setLayout(new BorderLayout());

        JLabel header =
                new JLabel(
                        "Customer Dashboard - " + user.getUsername(),
                        SwingConstants.CENTER
                );
        add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        tabs.add(
                "Products",
                new ProductListPanel(
                        productService,
                        cartService,
                        user
                )
        );

        tabs.add(
                "Cart",
                new CartPanel(
                        cartService,
                        productRepository,
                        authService,
                        orderService,
                        user.getId()
                )
        );

        tabs.add(
                "Orders",
                new OrderHistoryPanel(
                        authService,
                        orderService
                )
        );

        add(tabs, BorderLayout.CENTER);
    }
}
