package com.shoppingmall.ui;

import com.shoppingmall.model.Admin;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.*;
import com.shoppingmall.repository.impl.JsonCartRepository;
import com.shoppingmall.repository.impl.JsonProductRepository;
import com.shoppingmall.repository.impl.JsonUserRepository;
import com.shoppingmall.service.*;
import com.shoppingmall.ui.panels.*;
import com.shoppingmall.ui.util.LoginSuccessListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements LoginSuccessListener {

    private final CardLayout cardLayout;

    public MainFrame() {
        setTitle("Shopping Mall");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        UserRepository userRepository = new JsonUserRepository();


        if (userRepository.findAll().isEmpty()) {
            Admin admin = new Admin(
                    "1",
                    "admin",
                    "admin123"
            );
            userRepository.save(admin);
        }

        ProductRepository productRepository = new JsonProductRepository();
        CartRepository cartRepository = new JsonCartRepository();


        AuthService authService = new AuthService(userRepository);
        ProductService productService = new ProductService(productRepository);
        CartService cartService = new CartService(cartRepository);


        LoginPanel loginPanel = new LoginPanel(authService, this);
        add(loginPanel, "LOGIN");

        cardLayout.show(getContentPane(), "LOGIN");
    }

    @Override
    public void onLoginSuccess(User user) {


        JOptionPane.showMessageDialog(this,
                "Login successful: " + user.getUsername());

        if (user instanceof Admin) {
            AdminPanel adminPanel = new AdminPanel(user);
            add(adminPanel, "ADMIN");
            cardLayout.show(getContentPane(), "ADMIN");
        } else {
            CustomerPanel customerPanel = new CustomerPanel(user);
            add(customerPanel, "CUSTOMER");
            cardLayout.show(getContentPane(), "CUSTOMER");
        }
    }
}
