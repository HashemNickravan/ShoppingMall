package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Product;

import javax.swing.*;
import java.awt.*;

public class ProductForm extends JDialog {

    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField stockField;
    private boolean confirmed = false;
    private Product product;

    public ProductForm(Product product) {
        this.product = product;

        setModal(true);
        setSize(300, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            stockField.setText(String.valueOf(product.getStock()));
        }

        add(new JLabel("Name"));
        add(nameField);
        add(new JLabel("Price"));
        add(priceField);
        add(new JLabel("Stock"));
        add(stockField);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        add(ok);
        add(cancel);
    }

    public boolean showDialog() {
        setVisible(true);
        return confirmed;
    }

    public String getName() {
        return nameField.getText();
    }

    public long getPrice() {
        return Long.parseLong(priceField.getText());
    }

    public int getStock() {
        return Integer.parseInt(stockField.getText());
    }

    public Product getProduct() {
        return new Product(
                product.getId(),
                getName(),
                getPrice(),
                getStock(),
                null
        );
    }
}
