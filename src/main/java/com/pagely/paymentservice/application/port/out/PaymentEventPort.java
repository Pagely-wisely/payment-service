package com.pagely.paymentservice.application.port.out;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import com.pagely.paymentservice.domain.event.payload.PaymentConfirmFailedEvent;

public interface PaymentEventPort {
    void publishPaymentCompleted(PaymentCompletedEvent event);

    void publishPaymentConfirmFailed(PaymentConfirmFailedEvent event);
}
