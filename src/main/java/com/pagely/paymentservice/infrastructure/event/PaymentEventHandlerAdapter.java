package com.pagely.paymentservice.infrastructure.event;

import com.pagely.paymentservice.application.event.PaymentEventHandler;
import com.pagely.paymentservice.application.port.out.PaymentEventPort;
import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentEventHandlerAdapter implements PaymentEventHandler {

    private final PaymentEventPort paymentEventPort;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        paymentEventPort.publishPaymentCompleted(event);
    }
}
