package com.shoppingmall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shoppingmall.model.ExportFormat;
import com.shoppingmall.model.Order;
import com.shoppingmall.model.OrderItem;
import com.shoppingmall.repository.impl.JsonOrderRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderExportService {
    private final Gson gson;

    public OrderExportService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new JsonOrderRepository.LocalDateTimeAdapter())
                .create();
    }

    public void exportOrders(List<Order> orders, String filePath, ExportFormat format) throws IOException {
        switch (format) {
            case CSV:
                exportToCSV(orders, filePath);
                break;
            case JSON:
                exportToJSON(orders, filePath);
                break;
        }
    }

    private void exportToCSV(List<Order> orders, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Order ID,Username,Product ID,Product Name,Quantity,Price,Subtotal,Total Amount,Order Date\n");

            // Write data
            for (Order order : orders) {
                for (OrderItem item : order.getItems()) {
                    writer.append(order.getOrderId()).append(",");
                    writer.append(order.getUsername()).append(",");
                    writer.append(item.getProductId()).append(",");
                    writer.append("\"").append(item.getProductName()).append("\",");
                    writer.append(String.valueOf(item.getQuantity())).append(",");
                    writer.append(String.valueOf(item.getPriceAtPurchase())).append(",");
                    writer.append(String.valueOf(item.getSubtotal())).append(",");
                    writer.append(String.valueOf(order.getTotalAmount())).append(",");
                    writer.append(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    writer.append("\n");
                }
            }
        }
    }

    private void exportToJSON(List<Order> orders, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(orders, writer);
        }
    }
}
