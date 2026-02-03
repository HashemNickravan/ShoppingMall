package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.OrderItem;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.service.CartService;
import com.shoppingmall.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CartPanel extends JPanel {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final String userId;

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;

    public CartPanel(CartService cartService,
                     ProductRepository productRepository,
                     AuthService authService,
                     String userId) {

        this.cartService = cartService;
        this.productRepository = productRepository;
        this.authService = authService;
        this.userId = userId;

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(
                new Object[]{"Product", "Price", "Qty", "Total"}, 0
        );
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        totalLabel = new JLabel("Total: 0 Toman");

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.addActionListener(e -> checkout());

        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(checkoutBtn, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        Cart cart = cartService.getCartByUserId(userId);
        double total = 0;

        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            Product p = productRepository.findById(e.getKey()).orElse(null);
            if (p != null) {
                double subtotal = p.getPrice() * e.getValue();
                total += subtotal;
                tableModel.addRow(
                        new Object[]{p.getName(), p.getPrice(), e.getValue(), subtotal}
                );
            }
        }
        totalLabel.setText("Total: " + total + " Toman");
    }

    private void checkout() {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty");
            return;
        }

        List<OrderItem> items = new ArrayList<>();
        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            Product p = productRepository.findById(e.getKey()).orElse(null);
            if (p != null)
                items.add(new OrderItem(
                        p.getId(), p.getName(), p.getPrice(), e.getValue()
                ));
        }

        String username = authService.getCurrentUser().getUsername();
        OrderService.getInstance().createOrder(username, items);

        cartService.clearCart(userId);
        refresh();

        JOptionPane.showMessageDialog(this, "Order placed successfully");
    }
}
