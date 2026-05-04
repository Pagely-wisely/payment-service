package com.pagely.paymentservice.application.dto.command;

import java.util.UUID;

public record CreatePaymentCommand(
        UUID orderId,
        UUID buyerId,
        int price,
        UUID sellerId
) {
}
