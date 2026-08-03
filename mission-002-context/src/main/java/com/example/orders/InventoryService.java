package com.example.orders;

import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class InventoryService {
    private final Clock clock;
    private final Map<String, Integer> onHand = new HashMap<>();
    private final Map<UUID, Reservation> reservations = new HashMap<>();

    public InventoryService(Clock clock) {
        this.clock = clock;
    }

    public void receive(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        onHand.merge(sku, quantity, Integer::sum);
    }

    public boolean canReserve(Order order) {
        Map<String, Integer> requested = requestedQuantities(order);
        return requested.entrySet().stream()
                .allMatch(entry -> available(entry.getKey()) >= entry.getValue());
    }

    public Optional<Reservation> reserve(Order order) {
        Map<String, Integer> requested = requestedQuantities(order);
        if (!canReserve(order)) {
            return Optional.empty();
        }
        requested.forEach((sku, quantity) -> onHand.compute(sku, (ignored, current) -> current - quantity));
        Reservation reservation = new Reservation(order.id(), requested, Instant.now(clock));
        reservations.put(order.id(), reservation);
        return Optional.of(reservation);
    }

    public void release(UUID orderId) {
        Reservation reservation = reservations.remove(orderId);
        if (reservation != null) {
            reservation.quantities().forEach((sku, quantity) -> onHand.merge(sku, quantity, Integer::sum));
        }
    }

    public void commit(UUID orderId) {
        reservations.remove(orderId);
    }

    public int available(String sku) {
        return onHand.getOrDefault(sku, 0);
    }

    public boolean hasReservation(UUID orderId) {
        return reservations.containsKey(orderId);
    }

    private Map<String, Integer> requestedQuantities(Order order) {
        Map<String, Integer> requested = new HashMap<>();
        for (OrderLine line : order.lines()) {
            requested.merge(line.sku(), line.quantity(), Integer::sum);
        }
        return requested;
    }
}
