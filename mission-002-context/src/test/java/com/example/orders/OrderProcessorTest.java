package com.example.orders;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorTest {
    @Test
    void acceptsOrderWhenInventoryAndPaymentSucceed() {
        InventoryService inventory = inventory();
        inventory.receive("COFFEE", 4);
        PricingCatalog pricing = pricing();
        RecordingPaymentGateway payments = new RecordingPaymentGateway();
        OrderProcessor processor = new OrderProcessor(inventory, pricing, payments);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 2)));

        ProcessingResult result = processor.submit(order);

        assertEquals(OrderStatus.ACCEPTED, result.status());
        assertEquals(OrderStatus.ACCEPTED, order.status());
        assertEquals(2, inventory.available("COFFEE"));
        assertEquals(1, payments.authorizationCount());
        assertEquals(new BigDecimal("25.00"), payments.lastAmount());
    }

    @Test
    void backordersOrderWhenInventoryIsUnavailable() {
        InventoryService inventory = inventory();
        PricingCatalog pricing = pricing();
        RecordingPaymentGateway payments = new RecordingPaymentGateway();
        OrderProcessor processor = new OrderProcessor(inventory, pricing, payments);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 1)));

        ProcessingResult result = processor.submit(order);

        assertEquals(OrderStatus.BACKORDERED, result.status());
        assertEquals(OrderStatus.BACKORDERED, order.status());
        assertEquals(0, payments.authorizationCount());
    }

    @Test
    void cancelAcceptedOrderMakesReservedStockAvailableAgain() {
        InventoryService inventory = inventory();
        inventory.receive("COFFEE", 1);
        PricingCatalog pricing = pricing();
        RecordingPaymentGateway payments = new RecordingPaymentGateway();
        OrderProcessor processor = new OrderProcessor(inventory, pricing, payments);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 1)));
        processor.submit(order);

        processor.cancel(order);

        assertEquals(OrderStatus.CANCELLED, order.status());
        assertEquals(1, inventory.available("COFFEE"));
        assertFalse(inventory.hasReservation(order.id()));
    }

    @Test
    void shippedOrderLeavesPurchasedStockUnavailable() {
        InventoryService inventory = inventory();
        inventory.receive("COFFEE", 1);
        PricingCatalog pricing = pricing();
        RecordingPaymentGateway payments = new RecordingPaymentGateway();
        OrderProcessor processor = new OrderProcessor(inventory, pricing, payments);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 1)));
        processor.submit(order);

        processor.ship(order);

        assertEquals(OrderStatus.SHIPPED, order.status());
        assertEquals(0, inventory.available("COFFEE"));
        assertFalse(inventory.hasReservation(order.id()));
    }

    private static InventoryService inventory() {
        return new InventoryService(Clock.fixed(Instant.parse("2026-08-03T12:00:00Z"), ZoneOffset.UTC));
    }

    private static PricingCatalog pricing() {
        PricingCatalog catalog = new PricingCatalog();
        catalog.put("COFFEE", new BigDecimal("12.50"));
        return catalog;
    }
}
