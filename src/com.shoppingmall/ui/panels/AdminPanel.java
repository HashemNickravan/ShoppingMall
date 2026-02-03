package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel(User admin, ProductService productService) {
        setLayout(new BorderLayout());
        add(new JLabel("Admin Panel - " + admin.getUsername(),
                SwingConstants.CENTER), BorderLayout.NORTH);
    }
}
