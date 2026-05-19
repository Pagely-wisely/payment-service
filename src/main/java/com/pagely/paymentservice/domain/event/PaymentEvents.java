package com.pagely.paymentservice.domain.event;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import com.pagely.paymentservice.domain.event.payload.PaymentConfirmFailedEvent;

public interface PaymentEvents {
    void paymentCompleted(PaymentCompletedEvent event);

    void paymentConfirmFailed(PaymentConfirmFailedEvent event);
}
