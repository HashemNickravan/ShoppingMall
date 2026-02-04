package com.shoppingmall.ui.panels;

import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private MainFrame mainFrame;
    private ProductListPanel productListPanel;
    private CartPanel cartPanel;
    private OrderHistoryPanel orderHistoryPanel;

    public CustomerPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Customer Panel - Welcome " +
                mainFrame.getAuthService().getCurrentUser().getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        productListPanel = new ProductListPanel(mainFrame, this);
        cartPanel = new CartPanel(mainFrame, this);
        orderHistoryPanel = new OrderHistoryPanel(mainFrame);

        tabbedPane.addTab("Products", productListPanel);
        tabbedPane.addTab("Cart", cartPanel);
        tabbedPane.addTab("Orders", orderHistoryPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void handleLogout() {
        mainFrame.logout();
    }

    public void refreshCart() {
        cartPanel.loadCart();
    }

    public void refreshOrders() {
        orderHistoryPanel.loadOrders();
    }
}
