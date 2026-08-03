package com.example.orders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class PricingCatalog {
    private final Map<String, BigDecimal> prices = new HashMap<>();

    public void put(String sku, BigDecimal price) {
        if (price.signum() < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        prices.put(sku, price);
    }

    public BigDecimal total(Order order) {
        return order.lines().stream()
                .map(line -> priceOf(line.sku()).multiply(BigDecimal.valueOf(line.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal priceOf(String sku) {
        BigDecimal price = prices.get(sku);
        if (price == null) {
            throw new IllegalArgumentException("missing price for " + sku);
        }
        return price;
    }
}
