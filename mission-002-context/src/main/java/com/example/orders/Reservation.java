package com.example.orders;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public record Reservation(UUID orderId, Map<String, Integer> quantities, Instant createdAt) {
    public Reservation {
        Objects.requireNonNull(orderId, "orderId");
        Objects.requireNonNull(quantities, "quantities");
        Objects.requireNonNull(createdAt, "createdAt");
        quantities = Map.copyOf(quantities);
    }
}
