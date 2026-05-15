package com.pagely.paymentservice.application.service;

import com.pagely.paymentservice.application.dto.command.ConfirmPaymentCommand;
import com.pagely.paymentservice.application.dto.command.CreatePaymentCommand;
import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import com.pagely.paymentservice.domain.event.PaymentEvents;
import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import com.pagely.paymentservice.domain.model.Payment;
import com.pagely.paymentservice.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final PaymentProvider paymentProvider;
    private final PaymentEvents paymentEvents;

    @Transactional
    public void createPayment(CreatePaymentCommand command) {
        Payment payment = Payment.create(command.orderId(), command.buyerId(), command.sellerId(), command.price());
        paymentRepository.save(payment);
    }

    @Transactional
    public void validateAndMarkConfirmRequested(ConfirmPaymentCommand command) {
        Payment payment = paymentRepository.findByOrderId(command.orderId());

        payment.validateBuyer(command.buyerId());
        payment.validateAmount(command.price());
        payment.markConfirmRequested();
    }

    @Transactional
    public ConfirmPaymentResult applyConfirmedResult(
            ConfirmPaymentCommand command,
            PaymentProviderConfirmResult result
    ) {
        Payment payment = paymentRepository.findByOrderId(command.orderId());

        payment.validateConfirmResult(result); // PG 승인 응답값 검증
        payment.confirm(result);

        // 결제 완료 이벤트 발행
        paymentEvents.paymentCompleted(PaymentCompletedEvent.of(payment));

        return ConfirmPaymentResult.fromEntity(payment);
    }
}
