package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.ProductRepository;
import com.shoppingmall.service.CartService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class CartPanel extends JPanel {

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final String userId;

    private JTable table;
    private JLabel totalLabel;
    private JButton minusBtn;
    private JButton plusBtn;
    private JButton checkoutBtn;

    public CartPanel(String userId,
                     CartService cartService,
                     ProductRepository productRepository) {

        this.userId = userId;
        this.cartService = cartService;
        this.productRepository = productRepository;

        setLayout(new BorderLayout());

        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        JPanel buttons = new JPanel();
        minusBtn = new JButton("-");
        plusBtn = new JButton("+");
        checkoutBtn = new JButton("Checkout");

        buttons.add(minusBtn);
        buttons.add(plusBtn);
        buttons.add(checkoutBtn);

        totalLabel = new JLabel("Total: 0");
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        bottom.add(buttons, BorderLayout.WEST);
        bottom.add(totalLabel, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        minusBtn.addActionListener(e -> changeQty(-1));
        plusBtn.addActionListener(e -> changeQty(1));
        checkoutBtn.addActionListener(e -> checkout());

        table.getSelectionModel().addListSelectionListener(e -> updateButtons());

        refresh();
    }

    public void refresh() {
        Cart cart = cartService.getCartByUserId(userId);

        DefaultTableModel model =
                new DefaultTableModel(new Object[]{"Product", "Price", "Qty", "Subtotal"}, 0) {
                    public boolean isCellEditable(int r, int c) {
                        return false;
                    }
                };

        double total = 0;

        for (Map.Entry<String, Integer> e : cart.getItems().entrySet()) {
            String productId = e.getKey();
            int qty = e.getValue();

            Product product = productRepository.findById(productId).orElse(null);
            if (product == null) continue;

            double sub = product.getPrice() * qty;
            model.addRow(new Object[]{product, product.getPrice(), qty, sub});
            total += sub;
        }

        table.setModel(model);
        formatNumbers();
        totalLabel.setText("Total: " + String.format("%.2f", total));
        updateButtons();
    }

    private void changeQty(int delta) {
        int row = table.getSelectedRow();
        if (row == -1) return;

        Product product = (Product) table.getValueAt(row, 0);
        int qty = (int) table.getValueAt(row, 2);
        cartService.updateQuantity(userId, String.valueOf(product), qty + delta);
        refresh();
    }

    private void checkout() {
        cartService.clearCart(userId);
        refresh();
        JOptionPane.showMessageDialog(this, "Checkout completed");
    }

    private void updateButtons() {
        boolean selected = table.getSelectedRow() != -1;
        minusBtn.setEnabled(selected);
        plusBtn.setEnabled(selected);
        checkoutBtn.setEnabled(table.getRowCount() > 0);
    }

    private void formatNumbers() {
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(1).setCellRenderer(right);
        table.getColumnModel().getColumn(2).setCellRenderer(right);
        table.getColumnModel().getColumn(3).setCellRenderer(right);
    }
}
