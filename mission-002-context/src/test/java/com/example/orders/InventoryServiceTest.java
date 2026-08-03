package com.example.orders;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    private final InventoryService inventory = new InventoryService(Clock.fixed(Instant.parse("2026-08-03T12:00:00Z"), ZoneOffset.UTC));

    @Test
    void reservesAvailableItems() {
        inventory.receive("COFFEE", 3);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 2)));

        Reservation reservation = inventory.reserve(order).orElseThrow();

        assertEquals(order.id(), reservation.orderId());
        assertEquals(1, inventory.available("COFFEE"));
        assertTrue(inventory.hasReservation(order.id()));
    }

    @Test
    void rejectsReservationWhenAnySkuIsShort() {
        inventory.receive("COFFEE", 1);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(
                new OrderLine("COFFEE", 1),
                new OrderLine("FILTER", 1)));

        assertTrue(inventory.reserve(order).isEmpty());
        assertEquals(1, inventory.available("COFFEE"));
        assertFalse(inventory.hasReservation(order.id()));
    }

    @Test
    void releaseRestoresReservedItems() {
        inventory.receive("COFFEE", 2);
        Order order = new Order(UUID.randomUUID(), "customer-a", List.of(new OrderLine("COFFEE", 2)));
        inventory.reserve(order).orElseThrow();

        inventory.release(order.id());

        assertEquals(2, inventory.available("COFFEE"));
        assertFalse(inventory.hasReservation(order.id()));
    }
}
