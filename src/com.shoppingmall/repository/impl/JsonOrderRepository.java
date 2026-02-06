package com.shoppingmall.repository.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.shoppingmall.model.Order;
import com.shoppingmall.repository.OrderRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class JsonOrderRepository implements OrderRepository {
    private static final String FILE_PATH = "data/orders.json";
    private final Gson gson;
    private Map<String, Order> orders;

    public JsonOrderRepository() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.orders = new HashMap<>();
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            saveToFile();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, Order>>(){}.getType();
            Map<String, Order> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                orders = loaded;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(orders, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
        saveToFile();
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public List<Order> findByUsername(String username) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getUsername().equals(username)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(localDateTime.format(formatter));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
}
