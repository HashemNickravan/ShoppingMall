package com.shoppingmall.ui;

import com.shoppingmall.model.Role;
import com.shoppingmall.model.User;
import com.shoppingmall.repository.CartRepository;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.repository.UserRepository;
import com.shoppingmall.repository.OrderRepository;
import com.shoppingmall.repository.impl.InMemoryCartRepository;
import com.shoppingmall.repository.impl.InMemoryProductRepository;
import com.shoppingmall.repository.impl.InMemoryUserRepository;
import com.shoppingmall.repository.impl.InMemoryOrderRepository;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.OrderService;
import com.shoppingmall.service.ProductService;
import com.shoppingmall.ui.panels.AdminPanel;
import com.shoppingmall.ui.panels.CustomerPanel;
import com.shoppingmall.ui.panels.LoginPanel;
import com.shoppingmall.ui.util.LoginSuccessListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements LoginSuccessListener {

    private final CardLayout cardLayout;

    private final AuthService authService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final ProductRepository productRepository;

    public MainFrame() {

        UserRepository userRepository = new InMemoryUserRepository();
        productRepository = new InMemoryProductRepository();
        CartRepository cartRepository = new InMemoryCartRepository();
        OrderRepository orderRepository = new InMemoryOrderRepository();

        authService = new AuthService(userRepository);
        productService = new ProductService(productRepository);
        cartService = new CartService(cartRepository);
        orderService = new OrderService(orderRepository);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        add(new LoginPanel(authService, this), "LOGIN");
        cardLayout.show(getContentPane(), "LOGIN");

        setTitle("Shopping Mall");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void onLoginSuccess(User user) {
        if (user.getRole() == Role.ADMIN) {
            add(
                    new AdminPanel(user, productService),
                    "ADMIN"
            );
            cardLayout.show(getContentPane(), "ADMIN");
        } else {
            add(
                    new CustomerPanel(
                            user,
                            productService,
                            cartService,
                            productRepository,
                            authService,
                            orderService
                    ),
                    "CUSTOMER"
            );
            cardLayout.show(getContentPane(), "CUSTOMER");
        }
    }
}
