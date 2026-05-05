package com.pagely.paymentservice.presentation.dto.response;

import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.domain.model.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConfirmPaymentResponse(
        UUID id,
        UUID orderId,
        UUID buyerId,
        UUID sellerId,
        int amount,
        PaymentStatus status,
        String method,
        String pgProvider,
        String paymentKey,
        LocalDateTime pgApprovedAt
) {
    public static ConfirmPaymentResponse fromResult(ConfirmPaymentResult result) {
        return new ConfirmPaymentResponse(
                result.id(),
                result.orderId(),
                result.buyerId(),
                result.sellerId(),
                result.amount(),
                result.status(),
                result.method(),
                result.pgProvider(),
                result.paymentKey(),
                result.pgApprovedAt()
        );
    }
}
