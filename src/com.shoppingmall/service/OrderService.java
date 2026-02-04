package com.shoppingmall.service;

import com.shoppingmall.model.Cart;
import com.shoppingmall.model.Order;
import com.shoppingmall.model.OrderItem;
import com.shoppingmall.model.Product;
import com.shoppingmall.repository.OrderRepository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private static final String ORDER_COUNTER_FILE = "data/order_counter.txt";
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private int orderCounter;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.orderCounter = loadOrderCounter();
    }

    private int loadOrderCounter() {
        File file = new File(ORDER_COUNTER_FILE);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveOrderCounter(1);
            return 1;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null ? Integer.parseInt(line) : 1;
        } catch (IOException | NumberFormatException e) {
            return 1;
        }
    }

    private void saveOrderCounter(int counter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_COUNTER_FILE))) {
            writer.write(String.valueOf(counter));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateOrderId() {
        String orderId = String.format("ORD%05d", orderCounter);
        orderCounter++;
        saveOrderCounter(orderCounter);
        return orderId;
    }

    public Order createOrder(String username, Cart cart) {
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (String productId : cart.getItems().keySet()) {
            Product product = productService.getProductById(productId).orElse(null);
            if (product != null) {
                int quantity = cart.getItems().get(productId);
                OrderItem item = new OrderItem(
                        product.getId(),
                        product.getName(),
                        quantity,
                        product.getPrice()
                );
                orderItems.add(item);
                totalAmount += item.getSubtotal();
            }
        }

        Order order = new Order(
                generateOrderId(),
                username,
                orderItems,
                totalAmount,
                LocalDateTime.now()
        );

        orderRepository.save(order);
        return order;
    }

    public List<Order> getUserOrders(String username) {
        return orderRepository.findByUsername(username);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
