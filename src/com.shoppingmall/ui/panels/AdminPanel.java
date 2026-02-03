package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Product;
import com.shoppingmall.model.User;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {

    private final ProductService productService;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public AdminPanel(User admin, ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin - Product Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Price", "Stock"}, 0
        );
        table = new JTable(tableModel);
        refreshTable();

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);

        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addProduct());
        editBtn.addActionListener(e -> editProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getStock()
            });
        }
    }

    private void addProduct() {
        ProductForm form = new ProductForm(null);
        if (form.showDialog()) {
            productService.addProduct(
                    form.getName(),
                    form.getPrice(),
                    form.getStock()
            );
            refreshTable();
        }
    }

    private void editProduct() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        Product product = new Product(
                tableModel.getValueAt(row, 0).toString(),
                tableModel.getValueAt(row, 1).toString(),
                Long.parseLong(tableModel.getValueAt(row, 2).toString()),
                Integer.parseInt(tableModel.getValueAt(row, 3).toString()),
                null
        );

        ProductForm form = new ProductForm(product);
        if (form.showDialog()) {
            productService.updateProduct(form.getProduct());
            refreshTable();
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = tableModel.getValueAt(row, 0).toString();
        productService.deleteProduct(id);
        refreshTable();
    }
}
