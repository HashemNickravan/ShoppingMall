package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Product;
import com.shoppingmall.model.User;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductListPanel extends JPanel {

    private final ProductService productService;
    private final CartService cartService;
    private final User customer;

    private JTable table;
    private DefaultTableModel tableModel;

    public ProductListPanel(ProductService productService,
                            CartService cartService,
                            User customer) {

        this.productService = productService;
        this.cartService = cartService;
        this.customer = customer;

        setLayout(new BorderLayout());
        initUI();
        loadProducts();
    }

    private void initUI() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Price"}, 0
        );

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addToCartBtn = new JButton("Add to Cart");
        add(addToCartBtn, BorderLayout.SOUTH);

        addToCartBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a product first");
                return;
            }

            String productId =
                    tableModel.getValueAt(selectedRow, 0).toString();

            cartService.addToCart(customer.getId(), productId, 1);

            JOptionPane.showMessageDialog(this, "Added to cart");
        });
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice()
            });
        }
    }
}
