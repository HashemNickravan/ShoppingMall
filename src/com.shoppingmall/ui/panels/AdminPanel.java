package com.shoppingmall.ui.panels;

import com.shoppingmall.model.*;
import com.shoppingmall.service.*;
import com.shoppingmall.ui.MainFrame;
import com.shoppingmall.ui.dialogs.ProductFormDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class AdminPanel extends JPanel {
    private MainFrame mainFrame;
    private JTable productTable;
    private DefaultTableModel productTableModel;

    public AdminPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top panel with user info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Admin Panel - Welcome " +
                mainFrame.getAuthService().getCurrentUser().getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> mainFrame.logout());
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Product Management", createProductManagementPanel());
        tabbedPane.addTab("All Orders", createOrdersPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProductManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Image"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(productTableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> handleAddProduct());
        buttonPanel.add(addButton);

        JButton editButton = new JButton("Edit Product");
        editButton.addActionListener(e -> handleEditProduct());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> handleDeleteProduct());
        buttonPanel.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadProducts());
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Table
        String[] columns = {"Order ID", "Username", "Total Amount", "Date"};
        DefaultTableModel orderTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable orderTable = new JTable(orderTableModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Load orders
        List<Order> orders = mainFrame.getOrderService().getAllOrders();
        for (Order order : orders) {
            orderTableModel.addRow(new Object[]{
                    order.getOrderId(),
                    order.getUsername(),
                    String.format("%.2f", order.getTotalAmount()),
                    order.getOrderDate().toString()
            });
        }

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton exportCSVButton = new JButton("Export to CSV");
        exportCSVButton.addActionListener(e -> handleExportOrders(ExportFormat.CSV));
        buttonPanel.add(exportCSVButton);

        JButton exportJSONButton = new JButton("Export to JSON");
        exportJSONButton.addActionListener(e -> handleExportOrders(ExportFormat.JSON));
        buttonPanel.add(exportJSONButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void handleAddProduct() {
        ProductFormDialog dialog = new ProductFormDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Product product = dialog.getProduct();
            product.setId(mainFrame.getProductService().generateProductId());
            mainFrame.getProductService().addProduct(product);
            loadProducts();
            JOptionPane.showMessageDialog(this, "Product added successfully!");
        }
    }

    private void handleEditProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product to edit.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String productId = (String) productTableModel.getValueAt(selectedRow, 0);
        Product product = mainFrame.getProductService().getProductById(productId).orElse(null);

        if (product != null) {
            ProductFormDialog dialog = new ProductFormDialog((Frame) SwingUtilities.getWindowAncestor(this), product);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                mainFrame.getProductService().updateProduct(dialog.getProduct());
                loadProducts();
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
            }
        }
    }

    private void handleDeleteProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String productId = (String) productTableModel.getValueAt(selectedRow, 0);
            mainFrame.getProductService().deleteProduct(productId);
            loadProducts();
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
        }
    }

    private void handleExportOrders(ExportFormat format) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(
                "orders_export." + (format == ExportFormat.CSV ? "csv" : "json")));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                List<Order> orders = mainFrame.getOrderService().getAllOrders();
                mainFrame.getOrderExportService().exportOrders(
                        orders,
                        fileChooser.getSelectedFile().getAbsolutePath(),
                        format);
                JOptionPane.showMessageDialog(this, "Orders exported successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting orders: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProducts() {
        productTableModel.setRowCount(0);
        List<Product> products = mainFrame.getProductService().getAllProducts();

        for (Product product : products) {
            productTableModel.addRow(new Object[]{
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    String.format("%.2f", product.getPrice()),
                    product.getStock(),
                    product.getImagePath() != null ? "Yes" : "No"
            });
        }
    }
}
