package com.pagely.paymentservice.domain.event;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;

public interface PaymentEvents {
    void paymentCompleted(PaymentCompletedEvent event);
}
