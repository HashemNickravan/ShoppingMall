package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Category;
import com.shoppingmall.model.Product;
import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ProductListPanel extends JPanel {
    private MainFrame mainFrame;
    private CustomerPanel customerPanel;
    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> sortComboBox;
    private JPanel productGridPanel;

    public ProductListPanel(MainFrame mainFrame, CustomerPanel customerPanel) {
        this.mainFrame = mainFrame;
        this.customerPanel = customerPanel;
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());


        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        controlPanel.add(new JLabel("Search:"));
        searchField = new JTextField(20);
        controlPanel.add(searchField);

        controlPanel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("All");
        for (Category cat : Category.values()) {
            categoryComboBox.addItem(cat.toString());
        }
        controlPanel.add(categoryComboBox);

        controlPanel.add(new JLabel("Sort by:"));
        sortComboBox = new JComboBox<>(new String[]{"None", "Name", "Price", "Category"});
        controlPanel.add(sortComboBox);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> loadProducts());
        controlPanel.add(searchButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetFilters());
        controlPanel.add(resetButton);

        add(controlPanel, BorderLayout.NORTH);


        productGridPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productGridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void resetFilters() {
        searchField.setText("");
        categoryComboBox.setSelectedIndex(0);
        sortComboBox.setSelectedIndex(0);
        loadProducts();
    }

    private void loadProducts() {
        productGridPanel.removeAll();

        String keyword = searchField.getText().trim();
        String categoryStr = (String) categoryComboBox.getSelectedItem();
        Category category = categoryStr.equals("All") ? null : Category.valueOf(categoryStr);
        String sortBy = (String) sortComboBox.getSelectedItem();

        List<Product> products = mainFrame.getProductService().searchProducts(keyword, category);

        if (!sortBy.equals("None")) {
            products = mainFrame.getProductService().sortProducts(products, sortBy);
        }

        for (Product product : products) {
            productGridPanel.add(createProductCard(product));
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));


        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));

        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            File imageFile = new File(product.getImagePath());
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(product.getImagePath());
                Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("No Image");
            }
        } else {
            imageLabel.setText("No Image");
        }
        card.add(imageLabel, BorderLayout.NORTH);


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(nameLabel);

        JLabel categoryLabel = new JLabel("Category: " + product.getCategory());
        categoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(categoryLabel);

        JLabel priceLabel = new JLabel(String.format("Price: %.2f Toman", product.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(priceLabel);

        JLabel stockLabel = new JLabel("Stock: " + product.getStock());
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(stockLabel);

        card.add(infoPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, product.getStock(), 1));
        buttonPanel.add(quantitySpinner);

        JButton addButton = new JButton("Add to Cart");
        addButton.setEnabled(product.getStock() > 0);
        addButton.addActionListener(e -> {
            int quantity = (Integer) quantitySpinner.getValue();
            mainFrame.getCartService().addToCart(
                    mainFrame.getAuthService().getCurrentUser().getUsername(),
                    product.getId(),
                    quantity
            );
            JOptionPane.showMessageDialog(this, "Added to cart!");
            customerPanel.refreshCart();
        });
        buttonPanel.add(addButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }
}
