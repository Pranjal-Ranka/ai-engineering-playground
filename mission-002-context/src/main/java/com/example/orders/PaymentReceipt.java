package com.example.orders;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public record PaymentReceipt(UUID authorizationId, BigDecimal amount) {
    public PaymentReceipt {
        Objects.requireNonNull(authorizationId, "authorizationId");
        Objects.requireNonNull(amount, "amount");
    }
}
