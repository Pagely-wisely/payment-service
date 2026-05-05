package com.pagely.paymentservice.application.dto.result;

import java.time.LocalDateTime;

public record PaymentProviderConfirmResult(
        String paymentKey,
        String orderId,
        int totalAmount,
        String method,
        LocalDateTime approvedAt
) {
}
