package com.example.orders;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order {
    private final UUID id;
    private final String customerId;
    private final List<OrderLine> lines;
    private OrderStatus status;

    public Order(UUID id, String customerId, List<OrderLine> lines) {
        this.id = Objects.requireNonNull(id, "id");
        this.customerId = Objects.requireNonNull(customerId, "customerId");
        if (customerId.isBlank()) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines must not be empty");
        }
        this.lines = List.copyOf(lines);
        this.status = OrderStatus.DRAFT;
    }

    public UUID id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public OrderStatus status() {
        return status;
    }

    void markAccepted() {
        status = OrderStatus.ACCEPTED;
    }

    void markBackordered() {
        status = OrderStatus.BACKORDERED;
    }

    void markCancelled() {
        status = OrderStatus.CANCELLED;
    }

    void markShipped() {
        status = OrderStatus.SHIPPED;
    }
}
