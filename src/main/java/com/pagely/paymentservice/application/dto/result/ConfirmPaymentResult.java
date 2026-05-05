package com.pagely.paymentservice.application.dto.result;

import com.pagely.paymentservice.domain.model.Payment;
import com.pagely.paymentservice.domain.model.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConfirmPaymentResult(
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
    public static ConfirmPaymentResult fromEntity(Payment payment) {
        return new ConfirmPaymentResult(
                payment.getId(),
                payment.getOrderId(),
                payment.getBuyerId(),
                payment.getSellerId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getPgProvider(),
                payment.getPaymentKey(),
                payment.getPgApprovedAt()
        );
    }
}
