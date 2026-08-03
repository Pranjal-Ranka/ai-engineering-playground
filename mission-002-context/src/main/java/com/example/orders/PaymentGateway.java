package com.example.orders;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentReceipt authorize(String customerId, BigDecimal amount);
}
