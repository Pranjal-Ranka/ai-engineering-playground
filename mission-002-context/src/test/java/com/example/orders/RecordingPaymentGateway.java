package com.example.orders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class RecordingPaymentGateway implements PaymentGateway {
    private final List<BigDecimal> authorizations = new ArrayList<>();

    @Override
    public PaymentReceipt authorize(String customerId, BigDecimal amount) {
        authorizations.add(amount);
        return new PaymentReceipt(UUID.nameUUIDFromBytes((customerId + amount).getBytes()), amount);
    }

    int authorizationCount() {
        return authorizations.size();
    }

    BigDecimal lastAmount() {
        return authorizations.getLast();
    }
}
