package com.example.orders;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public record ProcessingResult(UUID orderId, OrderStatus status, PaymentReceipt receipt) {
    public ProcessingResult {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(status, "status");
    }

    public static ProcessingResult accepted(UUID orderId, PaymentReceipt receipt) {
        return new ProcessingResult(orderId, OrderStatus.ACCEPTED, Objects.requireNonNull(receipt, "receipt"));
    }

    public static ProcessingResult backordered(UUID orderId) {
        return new ProcessingResult(orderId, OrderStatus.BACKORDERED, null);
    }

    public Optional<PaymentReceipt> authorizedPayment() {
        return Optional.ofNullable(receipt);
    }
}
