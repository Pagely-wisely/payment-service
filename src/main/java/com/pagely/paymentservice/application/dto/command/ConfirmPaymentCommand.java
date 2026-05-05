package com.pagely.paymentservice.application.dto.command;

import java.util.UUID;

public record ConfirmPaymentCommand(
        String paymentKey,
        UUID orderId,
        int price,
        UUID buyerId
) {
}
