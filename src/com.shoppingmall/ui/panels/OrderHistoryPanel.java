package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Order;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.OrderService;
import com.shoppingmall.ui.dialogs.OrderDetailsDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class OrderHistoryPanel extends JPanel {

    public OrderHistoryPanel(AuthService authService, OrderService orderService) {

        setLayout(new BorderLayout());

        DefaultListModel<Order> model = new DefaultListModel<>();
        JList<Order> list = new JList<>(model);

        add(new JScrollPane(list), BorderLayout.CENTER);

        List<Order> orders =
                orderService.getOrdersByUser(
                        authService.getCurrentUser().getUsername()
                );

        for (Order o : orders) {
            model.addElement(o);
        }

        list.setCellRenderer((l, v, i, s, f) ->
                new JLabel(
                        "Order #" + v.getId() +
                                " | " + v.getTotalPrice() + " Toman"
                )
        );

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Order selected = list.getSelectedValue();
                    if (selected != null) {
                        new OrderDetailsDialog(
                                SwingUtilities.getWindowAncestor(list),
                                selected.getItems()
                        ).setVisible(true);
                    }
                }
            }
        });
    }
}
