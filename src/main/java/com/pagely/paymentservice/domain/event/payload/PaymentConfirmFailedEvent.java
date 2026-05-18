package com.pagely.paymentservice.domain.event.payload;

import com.pagely.paymentservice.domain.event.BaseEvent;
import com.pagely.paymentservice.domain.model.Payment;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PaymentConfirmFailedEvent extends BaseEvent {

    private static final String DOMAIN_TYPE = "PAYMENT";

    private PaymentConfirmFailedEvent(UUID orderId, Object payload) {
        super(DOMAIN_TYPE, orderId, payload);
    }

    public static PaymentConfirmFailedEvent of(Payment payment) {
        return new PaymentConfirmFailedEvent(
                payment.getId(),
                new Payload(
                        payment.getOrderId(),
                        payment.getId(),
                        payment.getAmount()
                )
        );
    }

    public record Payload(
            UUID orderId,
            UUID paymentId,
            int amount
    ) {
    }
}
