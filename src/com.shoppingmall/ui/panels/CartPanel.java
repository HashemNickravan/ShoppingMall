package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.Product;
import com.shoppingmall.model.User;
import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class CartPanel extends JPanel {

    private MainFrame mainFrame;
    private CustomerPanel customerPanel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;
    private JLabel totalLabel;
    private JLabel balanceLabel;

    public CartPanel(MainFrame mainFrame, CustomerPanel customerPanel) {
        this.mainFrame = mainFrame;
        this.customerPanel = customerPanel;
        initializeUI();
        loadCart();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(balanceLabel);
        add(topPanel, BorderLayout.NORTH);


        String[] columns = {"Product", "Price", "Quantity", "Subtotal"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Integer.class : String.class;
            }
        };

        cartTable = new JTable(cartTableModel);


        add(new JScrollPane(cartTable), BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: 0.00 Toman", SwingConstants.RIGHT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));



        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(e -> handleRemoveItem());
        buttonPanel.add(removeButton);

        JButton clearButton = new JButton("Clear Cart");
        clearButton.addActionListener(e -> handleClearCart());
        buttonPanel.add(clearButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> handleCheckout());
        buttonPanel.add(checkoutButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void loadCart() {
        User user = mainFrame.getAuthService().getCurrentUser();
        balanceLabel.setText(String.format("Your Balance: %.2f Toman", user.getBalance()));

        cartTableModel.setRowCount(0);
        Cart cart = mainFrame.getCartService().getOrCreateCart(user.getUsername());

        double total = 0;
        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            Product product = mainFrame.getProductService()
                    .getProductById(entry.getKey()).orElse(null);

            if (product != null) {
                int qty = entry.getValue();
                double sub = product.getPrice() * qty;
                total += sub;

                cartTableModel.addRow(new Object[]{
                        product.getName(),
                        String.format("%.2f", product.getPrice()),
                        qty,
                        String.format("%.2f", sub)
                });
            }
        }
        totalLabel.setText(String.format("Total: %.2f Toman", total));
    }



    private void handleRemoveItem() {
        int row = cartTable.getSelectedRow();
        if (row < 0) return;

        String name = (String) cartTableModel.getValueAt(row, 0);
        User user = mainFrame.getAuthService().getCurrentUser();
        Cart cart = mainFrame.getCartService().getOrCreateCart(user.getUsername());

        for (String productId : cart.getItems().keySet()) {
            Product product = mainFrame.getProductService()
                    .getProductById(productId).orElse(null);
            if (product != null && product.getName().equals(name)) {
                mainFrame.getCartService().removeFromCart(user.getUsername(), productId);
                break;
            }
        }
        loadCart();
    }

    private void handleClearCart() {
        User user = mainFrame.getAuthService().getCurrentUser();
        mainFrame.getCartService().clearCart(user.getUsername());
        loadCart();
    }

    private void handleCheckout() {
        User user = mainFrame.getAuthService().getCurrentUser();
        Cart cart = mainFrame.getCartService().getOrCreateCart(user.getUsername());


        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double total = mainFrame.getCartService().calculateTotal(user.getUsername());
        if (user.getBalance() < total) {
            JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        mainFrame.getAuthService().updateUserBalance(user.getBalance() - total);


        for (String productId : cart.getItems().keySet()) {
            Product product = mainFrame.getProductService()
                    .getProductById(productId).orElse(null);
            if (product != null) {
                int qty = cart.getItems().get(productId);
                product.setStock(product.getStock() - qty);
                mainFrame.getProductService().updateProduct(product);
            }
        }


        mainFrame.getOrderService().createOrder(user.getUsername(), cart);
        mainFrame.getCartService().clearCart(user.getUsername());

        loadCart();
        customerPanel.refreshOrders();
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
    }
}
