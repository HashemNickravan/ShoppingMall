package com.shoppingmall.model;

public class Customer extends User {

    public Customer(String id, String username, String password) {
        super(id, username, password, "CUSTOMER");
    }
}
