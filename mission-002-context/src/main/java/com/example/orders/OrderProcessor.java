package com.example.orders;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderProcessor {
    private final InventoryService inventory;
    private final PricingCatalog pricing;
    private final PaymentGateway payments;

    public OrderProcessor(InventoryService inventory, PricingCatalog pricing, PaymentGateway payments) {
        this.inventory = Objects.requireNonNull(inventory, "inventory");
        this.pricing = Objects.requireNonNull(pricing, "pricing");
        this.payments = Objects.requireNonNull(payments, "payments");
    }

    public ProcessingResult submit(Order order) {
        Objects.requireNonNull(order, "order");
        if (order.status() != OrderStatus.DRAFT) {
            throw new IllegalStateException("order must be draft");
        }
        if (inventory.reserve(order).isEmpty()) {
            order.markBackordered();
            return ProcessingResult.backordered(order.id());
        }
        BigDecimal total = pricing.total(order);
        PaymentReceipt receipt = payments.authorize(order.customerId(), total);
        order.markAccepted();
        return ProcessingResult.accepted(order.id(), receipt);
    }

    public void cancel(Order order) {
        if (order.status() == OrderStatus.ACCEPTED || order.status() == OrderStatus.BACKORDERED) {
            inventory.release(order.id());
            order.markCancelled();
        }
    }

    public void ship(Order order) {
        if (order.status() != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("order must be accepted");
        }
        inventory.release(order.id());
        order.markShipped();
    }
}
