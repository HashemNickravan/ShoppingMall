package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Order;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderHistoryPanel extends JPanel {

    public OrderHistoryPanel(AuthService authService, OrderService orderService) {

        setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);

        List<Order> orders =
                orderService.getOrdersByUser(
                        authService.getCurrentUser().getUsername()
                );

        for (Order o : orders) {
            model.addElement(
                    "Order #" + o.getId() + " | " + o.getTotalPrice() + " Toman"
            );
        }
    }
}
