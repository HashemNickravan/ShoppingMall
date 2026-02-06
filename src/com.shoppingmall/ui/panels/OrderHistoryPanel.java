package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Order;
import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable orderTable;
    private DefaultTableModel orderTableModel;

    public OrderHistoryPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
        loadOrders();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());


        String[] columns = {"Order ID", "Date", "Total Amount (Toman)", "Items Count"};
        orderTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrders());
        buttonPanel.add(refreshButton);



        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadOrders() {
        orderTableModel.setRowCount(0);

        String username = mainFrame.getAuthService().getCurrentUser().getUsername();
        List<Order> orders;


        if (mainFrame.getAuthService().isAdmin()) {
            orders = mainFrame.getOrderService().getAllOrders();
        } else {
            orders = mainFrame.getOrderService().getUserOrders(username);
        }

        for (Order order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getOrderDate().toString(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getItems().size()
            });
        }
    }
}
