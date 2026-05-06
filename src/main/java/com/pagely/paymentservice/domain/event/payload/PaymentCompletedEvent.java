package com.pagely.paymentservice.domain.event.payload;

import com.pagely.paymentservice.domain.event.BaseEvent;
import com.pagely.paymentservice.domain.model.Payment;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PaymentCompletedEvent extends BaseEvent {

    private static final String DOMAIN_TYPE = "PAYMENT";

    private PaymentCompletedEvent(UUID orderId, Object payload) {
        super(DOMAIN_TYPE, orderId, payload);
    }

    public static PaymentCompletedEvent of(Payment payment) {
        return new PaymentCompletedEvent(
                payment.getId(),
                new Payload(
                        payment.getOrderId(),
                        payment.getId(),
                        payment.getBuyerId(),
                        payment.getSellerId(),
                        payment.getAmount(),
                        payment.getPgApprovedAt().toString()
                )
        );
    }

    public record Payload(
            UUID orderId,
            UUID paymentId,
            UUID buyerId,
            UUID sellerId,
            int amount,
            String approvedAt
    ) {
    }
}
