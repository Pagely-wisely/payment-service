package com.pagely.paymentservice.application.service;

import com.pagely.paymentservice.application.dto.command.ConfirmPaymentCommand;
import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCommandFacade {

    private final PaymentCommandService paymentCommandService;
    private final PaymentQueryService paymentQueryService;
    private final PaymentProvider paymentProvider;

    public ConfirmPaymentResult confirmPayment(ConfirmPaymentCommand command) {

        // 이미 완료된 결제면 DB 조회 결과 그대로 반환 (멱등성 보장)
        ConfirmPaymentResult idempotentResult = paymentQueryService.findIfAlreadyCompleted(command.orderId());

        if (idempotentResult != null) {
            return idempotentResult;
        }

        paymentCommandService.validateAndMarkConfirmRequested(command);

        PaymentProviderConfirmResult pgResult = requestPgConfirm(command);

        return paymentCommandService.applyConfirmedResult(command, pgResult);
    }

    // PG 승인 요청
    private PaymentProviderConfirmResult requestPgConfirm(ConfirmPaymentCommand command) {
        try {
            return paymentProvider.confirm(
                    command.paymentKey(),
                    command.orderId().toString(),
                    command.price()
            );
        } catch (Exception e) {
            paymentCommandService.markAsFailed(command.orderId());

            // TODO: 승인 실패 이벤트 발행

            throw new RuntimeException(e);
        }
    }
}
