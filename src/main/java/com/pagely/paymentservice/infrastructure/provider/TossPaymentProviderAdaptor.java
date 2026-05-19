package com.pagely.paymentservice.infrastructure.provider;

import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import com.pagely.paymentservice.infrastructure.client.pg.TossPaymentClient;
import com.pagely.paymentservice.infrastructure.client.pg.dto.TossConfirmRequest;
import com.pagely.paymentservice.infrastructure.client.pg.dto.TossConfirmResponse;
import com.pagely.paymentservice.application.exception.PgSystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentProviderAdaptor implements PaymentProvider {

    private final TossPaymentClient tossPaymentClient;

    /**
     * <pre>
     * PgSystemException(기술적 오류)에 한해 최대 3회 재시도.
     * backoff: 1000ms → 2000ms → 4000ms (multiplier=2, max=5000ms)
     * PgRejectedException은 재시도 대상에서 제외
     * </pre>
     */
    @Retryable(
            retryFor = PgSystemException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2, maxDelay = 5000L)
    )
    @Override
    public PaymentProviderConfirmResult confirm(String paymentKey, String orderId, int amount) {
        log.info("[Toss] 결제 승인 요청. orderId={}", orderId);
        TossConfirmRequest request = new TossConfirmRequest(paymentKey, orderId, amount);
        TossConfirmResponse response = tossPaymentClient.confirm(request);
        log.info("[Toss 결제 승인 response={}", response);
        return response.toResult();
    }

    /**
     * <pre>
     * 3회 재시도 모두 소진 시 호출.
     * </pre>
     */
    @Recover
    public PaymentProviderConfirmResult recoverFromSystemError(
            PgSystemException e,
            String paymentKey,
            String orderId,
            int amount
    ) {
        log.error("[Toss] 재시도 3회 소진. orderId={}, code={}", orderId, e.getPgCode());
        throw e;
    }

    @Override
    public void cancel(String paymentKey) {

    }
}
