package com.pagely.paymentservice.infrastructure.client.pg;

public record TossConfirmRequest(
        String paymentKey,
        String orderId,
        int amount
) {
}
