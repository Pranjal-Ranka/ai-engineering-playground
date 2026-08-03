package com.example.orders;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingCatalogTest {
    @Test
    void totalsLineItems() {
        PricingCatalog catalog = new PricingCatalog();
        catalog.put("COFFEE", new BigDecimal("12.50"));
        catalog.put("FILTER", new BigDecimal("3.25"));
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(
                new OrderLine("COFFEE", 2),
                new OrderLine("FILTER", 1)));

        assertEquals(new BigDecimal("28.25"), catalog.total(order));
    }
}
