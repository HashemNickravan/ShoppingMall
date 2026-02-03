package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Admin;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final Admin admin;
    private final ProductService productService;

    public AdminPanel(Admin admin, ProductService productService) {
        this.admin = admin;
        this.productService = productService;

        setLayout(new BorderLayout());
        add(new JLabel("Admin Panel - " + admin.getUsername()), BorderLayout.NORTH);
    }
}
