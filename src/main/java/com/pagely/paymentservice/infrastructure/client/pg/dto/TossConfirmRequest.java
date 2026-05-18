package com.pagely.paymentservice.infrastructure.client.pg.dto;

public record TossConfirmRequest(
        String paymentKey,
        String orderId,
        int amount
) {
}
