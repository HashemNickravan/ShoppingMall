package com.shoppingmall.ui;

import com.shoppingmall.repository.*;
import com.shoppingmall.repository.impl.*;
import com.shoppingmall.service.*;
import com.shoppingmall.ui.panels.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    private AuthService authService;
    private ProductService productService;
    private CartService cartService;
    private OrderService orderService;

    public MainFrame() {
        initializeRepositories();
        initializeServices();
        initializeUI();
    }

    private void initializeRepositories() {
        userRepository = new JsonUserRepository();
        productRepository = new JsonProductRepository();
        cartRepository = new JsonCartRepository();
        orderRepository = new JsonOrderRepository();
    }

    private void initializeServices() {
        authService = new AuthService(userRepository);
        productService = new ProductService(productRepository);
        cartService = new CartService(cartRepository, productService);
        orderService = new OrderService(orderRepository, productService);
    }

    private void initializeUI() {
        setTitle("Shopping Mall System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new LoginPanel(this), "LOGIN");
        contentPanel.add(new RegisterPanel(this), "REGISTER");

        add(contentPanel);
        showLoginPanel();
    }

    public void showLoginPanel() {
        cardLayout.show(contentPanel, "LOGIN");
    }

    public void showRegisterPanel() {
        cardLayout.show(contentPanel, "REGISTER");
    }

    public void showCustomerPanel() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof CustomerPanel) {
                contentPanel.remove(comp);
            }
        }

        CustomerPanel customerPanel = new CustomerPanel(this);
        contentPanel.add(customerPanel, "CUSTOMER");
        cardLayout.show(contentPanel, "CUSTOMER");
    }

    public void showAdminPanel() {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof AdminPanel) {
                contentPanel.remove(comp);
            }
        }

        AdminPanel adminPanel = new AdminPanel(this);
        contentPanel.add(adminPanel, "ADMIN");
        cardLayout.show(contentPanel, "ADMIN");
    }

    public void logout() {
        authService.logout();

        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof CustomerPanel || comp instanceof AdminPanel) {
                contentPanel.remove(comp);
            }
        }

        showLoginPanel();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }
}