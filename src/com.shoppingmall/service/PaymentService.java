package com.shoppingmall.service;

import com.shoppingmall.model.PaymentResult;

public class PaymentService {

    public PaymentResult pay(double amount) {
        if (amount <= 0) {
            return new PaymentResult(false, "Invalid amount");
        }
        return new PaymentResult(true, "Payment successful");
    }
}
