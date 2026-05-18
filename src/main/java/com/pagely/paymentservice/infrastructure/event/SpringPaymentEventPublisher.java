package com.pagely.paymentservice.infrastructure.event;

import com.pagely.paymentservice.domain.event.PaymentEvents;
import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import com.pagely.paymentservice.domain.event.payload.PaymentConfirmFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringPaymentEventPublisher implements PaymentEvents {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void paymentCompleted(PaymentCompletedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void paymentConfirmFailed(PaymentConfirmFailedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
