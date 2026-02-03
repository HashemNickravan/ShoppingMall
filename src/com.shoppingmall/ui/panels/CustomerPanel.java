package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;

import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {

    public CustomerPanel(User user) {
        setLayout(new BorderLayout());
        add(new JLabel("Customer Dashboard - " + user.getUsername(),
                SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
