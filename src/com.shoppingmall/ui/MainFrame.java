package com.shoppingmall.ui;

import com.shoppingmall.model.Role;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.repository.UserRepository;
import com.shoppingmall.repository.impl.JsonCartRepository;
import com.shoppingmall.repository.impl.JsonProductRepository;
import com.shoppingmall.repository.impl.JsonUserRepository;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ProductService;
import com.shoppingmall.ui.panels.AdminPanel;
import com.shoppingmall.ui.panels.CustomerPanel;
import com.shoppingmall.ui.panels.LoginPanel;
import com.shoppingmall.ui.util.LoginSuccessListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements LoginSuccessListener {

    private final CardLayout cardLayout;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    private final AuthService authService;
    private final ProductService productService;
    private final CartService cartService;

    public MainFrame() {
        setTitle("Shopping Mall");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        userRepository = new JsonUserRepository();
        productRepository = new JsonProductRepository();
        cartRepository = new JsonCartRepository();

        authService = new AuthService(userRepository);
        productService = new ProductService(productRepository);
        cartService = new CartService(cartRepository);

        if (userRepository.findAll().isEmpty()) {
            userRepository.save(
                    new User("1", "admin", "admin123", Role.ADMIN, 0)
            );
        }

        if (productService.getAllProducts().isEmpty()) {
            productService.addProduct("Laptop", 45000, 10);
            productService.addProduct("Mouse", 500, 50);
            productService.addProduct("Keyboard", 1200, 30);
        }

        LoginPanel loginPanel = new LoginPanel(authService, this);
        add(loginPanel, "LOGIN");

        cardLayout.show(getContentPane(), "LOGIN");
    }

    @Override
    public void onLoginSuccess(User user) {

        if (user.getRole() == Role.ADMIN) {
            AdminPanel adminPanel =
                    new AdminPanel(user, productService);

            add(adminPanel, "ADMIN");
            cardLayout.show(getContentPane(), "ADMIN");

        } else {
            CustomerPanel customerPanel =
                    new CustomerPanel(
                            user,
                            productService,
                            cartService,
                            productRepository
                    );

            add(customerPanel, "CUSTOMER");
            cardLayout.show(getContentPane(), "CUSTOMER");
        }
    }
}
