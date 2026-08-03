package com.example.orders;

import java.util.Objects;

public record OrderLine(String sku, int quantity) {
    public OrderLine {
        Objects.requireNonNull(sku, "sku");
        if (sku.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
    }
}
