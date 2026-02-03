package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Customer;
import com.shoppingmall.model.User;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    public CustomerPanel(User user,
                         ProductService productService,
                         CartService cartService) {

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
                        (Customer) user
                );
        add(productListPanel, BorderLayout.CENTER);

        CartPanel cartPanel = new CartPanel(
                ((Customer) user).getId(),
                cartService
        );
        add(cartPanel, BorderLayout.SOUTH);
    }
}
