package com.pagely.paymentservice.application.service;

import com.pagely.paymentservice.application.dto.command.ConfirmPaymentCommand;
import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
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
            log.error("[PaymentCommandFacade] PG 결제 승인 API 실패 orderId={} paymentKey={} cause={}",
                    command.orderId(), command.paymentKey(), e.getMessage(), e);

            try {
                paymentCommandService.markAsFailed(command.orderId(), "PG 결제 승인 실패");
            } catch (Exception ex) {
                log.error("[PaymentCommandFacade] 실패 상태 DB 저장 실패 orderId={}", command.orderId(), ex);
                throw ex;
            }

            // TODO: 승인 실패 이벤트 발행

            throw e;
        }
    }
}
