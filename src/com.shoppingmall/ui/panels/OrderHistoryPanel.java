package com.shoppingmall.ui.panels;

import com.shoppingmall.model.ExportFormat;
import com.shoppingmall.model.Order;
import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
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

        // Table
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

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrders());
        buttonPanel.add(refreshButton);

        JButton exportCSVButton = new JButton("Export to CSV");
        exportCSVButton.addActionListener(e -> handleExport(ExportFormat.CSV));
        buttonPanel.add(exportCSVButton);

        JButton exportJSONButton = new JButton("Export to JSON");
        exportJSONButton.addActionListener(e -> handleExport(ExportFormat.JSON));
        buttonPanel.add(exportJSONButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadOrders() {
        orderTableModel.setRowCount(0);

        String username = mainFrame.getAuthService().getCurrentUser().getUsername();
        List<Order> orders = mainFrame.getOrderService().getUserOrders(username);

        for (Order order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getOrderDate().toString(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getItems().size()
            });
        }
    }

    private void handleExport(ExportFormat format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(
                "my_orders." + (format == ExportFormat.CSV ? "csv" : "json")));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String username = mainFrame.getAuthService().getCurrentUser().getUsername();
                List<Order> orders = mainFrame.getOrderService().getUserOrders(username);

                mainFrame.getOrderExportService().exportOrders(
                        orders,
                        fileChooser.getSelectedFile().getAbsolutePath(),
                        format);

                JOptionPane.showMessageDialog(this,
                        "Orders exported successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting orders: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
