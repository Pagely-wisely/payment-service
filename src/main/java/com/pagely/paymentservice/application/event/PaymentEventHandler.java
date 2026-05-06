package com.pagely.paymentservice.application.event;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;

public interface PaymentEventHandler {
    void handlePaymentCompleted(PaymentCompletedEvent event);
}
