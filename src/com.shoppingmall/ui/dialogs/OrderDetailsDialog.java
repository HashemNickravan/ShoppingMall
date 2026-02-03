package com.shoppingmall.ui.dialogs;

import com.shoppingmall.model.OrderItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderDetailsDialog extends JDialog {

    public OrderDetailsDialog(Window owner, List<OrderItem> items) {
        super(owner, "Order Details", ModalityType.APPLICATION_MODAL);

        setSize(500, 300);
        setLocationRelativeTo(owner);

        String[] columns = {"Product", "Qty", "Unit Price", "Total"};
        Object[][] data = new Object[items.size()][4];

        for (int i = 0; i < items.size(); i++) {
            OrderItem it = items.get(i);
            data[i][0] = it.getProductName();
            data[i][1] = it.getQuantity();
            data[i][2] = it.getPrice();
            data[i][3] = it.getTotalPrice();
        }

        JTable table = new JTable(data, columns);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
