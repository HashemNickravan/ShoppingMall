package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    public AdminPanel(User user) {
        setLayout(new BorderLayout());
        add(new JLabel("Admin Dashboard - " + user.getUsername(),
                SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
