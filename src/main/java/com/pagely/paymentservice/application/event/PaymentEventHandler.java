package com.pagely.paymentservice.application.event;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import com.pagely.paymentservice.domain.event.payload.PaymentConfirmFailedEvent;

public interface PaymentEventHandler {
    void handlePaymentCompleted(PaymentCompletedEvent event);

    void handlePaymentConfirmFailed(PaymentConfirmFailedEvent event);
}
