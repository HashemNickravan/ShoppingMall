package com.shoppingmall.ui.dialogs;

import com.shoppingmall.model.Category;
import com.shoppingmall.model.Product;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ProductFormDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<Category> categoryComboBox;
    private JTextField priceField;
    private JTextField stockField;
    private JLabel imagePathLabel;
    private JButton selectImageButton;
    private String selectedImagePath;

    private Product product;
    private boolean confirmed;

    public ProductFormDialog(Frame parent, Product product) {
        super(parent, product == null ? "Add Product" : "Edit Product", true);
        this.product = product;
        this.confirmed = false;
        initializeUI();

        if (product != null) {
            loadProductData();
        }
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Name:"), gbc);

        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Category:"), gbc);

        categoryComboBox = new JComboBox<>(Category.values());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(categoryComboBox, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Price (Toman):"), gbc);

        priceField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(priceField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Stock:"), gbc);

        stockField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(stockField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Image:"), gbc);

        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
        imagePathLabel = new JLabel("No image selected");
        selectImageButton = new JButton("Select Image");
        selectImageButton.addActionListener(e -> selectImage());
        imagePanel.add(imagePathLabel, BorderLayout.CENTER);
        imagePanel.add(selectImageButton, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(imagePanel, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> handleSave());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png", "gif"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();


            try {
                File imagesDir = new File("product_images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destFile = new File(imagesDir, fileName);

                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedImagePath = "product_images/" + fileName;
                imagePathLabel.setText(fileName);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error copying image: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadProductData() {
        nameField.setText(product.getName());
        categoryComboBox.setSelectedItem(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));

        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            selectedImagePath = product.getImagePath();
            File imageFile = new File(selectedImagePath);
            imagePathLabel.setText(imageFile.getName());
        }
    }

    private void handleSave() {
        String name = nameField.getText().trim();
        Category category = (Category) categoryComboBox.getSelectedItem();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();


        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Product name cannot be empty.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid price (non-negative number).",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid stock (non-negative integer).",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (product == null) {
            product = new Product();
        }

        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setStock(stock);
        product.setImagePath(selectedImagePath);

        confirmed = true;
        dispose();
    }

    public Product getProduct() {
        return product;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
