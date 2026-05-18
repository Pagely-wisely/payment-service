package com.pagely.paymentservice.application.service;

import com.pagely.common.exception.BusinessException;
import com.pagely.paymentservice.application.dto.command.ConfirmPaymentCommand;
import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.exception.PgAlreadyProcessedException;
import com.pagely.paymentservice.application.exception.PgRejectedException;
import com.pagely.paymentservice.application.exception.PgResponseParseException;
import com.pagely.paymentservice.application.exception.PgSystemException;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import com.pagely.paymentservice.domain.exception.PaymentErrorCode;
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

        // Toss 결제 승인 API 호출
        try {
            PaymentProviderConfirmResult pgResult = paymentProvider.confirm(
                    command.paymentKey(),
                    command.orderId().toString(),
                    command.price()
            );

            return paymentCommandService.applyConfirmedResult(command, pgResult);

        } catch (PgAlreadyProcessedException e) {
            // TODO: Toss 결제 조회 API로 상태 동기화 필요
            log.warn("[Payment] PG 이미 처리된 결제. DB 동기화 진행 orderId={}", command.orderId());
//            PaymentProviderConfirmResult pgResult = paymentProvider.getConfirmResult(command.paymentKey());
//            return paymentCommandService.applyConfirmedResult(command, pgResult);
            throw e;
        } catch (PgRejectedException e) {
            // 4XX 에러 PG에서 거절했으므로 FAILED 처리
            log.warn("[Payment] PG 거절. orderId={}, code={}, message={}"
                    , command.orderId(), e.getPgCode(), e.getMessage());
            paymentCommandService.handleConfirmFailure(command.orderId(), "PG 거절: " + e.getPgCode());
            throw new BusinessException(PaymentErrorCode.PG_REJECTED);

        } catch (PgSystemException e) {
            // 재시도 3회 소진 → FAILED 처리
            log.error("[Payment] PG 시스템 오류. orderId={}, code={}, message={}"
                    , command.orderId(), e.getPgCode(), e.getMessage());
            paymentCommandService.handleConfirmFailure(command.orderId(), "PG 시스템 오류: " + e.getPgCode());
            throw new BusinessException(PaymentErrorCode.PG_SYSTEM_ERROR);

        } catch (PgResponseParseException e) {
            log.error("[Payment] PG 응답 파싱 실패. orderId={}", command.orderId());
            throw new BusinessException(PaymentErrorCode.PG_SYSTEM_ERROR);
        }
    }

}
