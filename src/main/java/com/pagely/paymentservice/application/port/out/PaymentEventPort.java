package com.pagely.paymentservice.application.port.out;

import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;

public interface PaymentEventPort {
    void publishPaymentCompleted(PaymentCompletedEvent event);

}
