package com.pagely.paymentservice.infrastructure.provider;

import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import com.pagely.paymentservice.application.port.out.PaymentProvider;
import com.pagely.paymentservice.infrastructure.client.pg.TossConfirmRequest;
import com.pagely.paymentservice.infrastructure.client.pg.TossConfirmResponse;
import com.pagely.paymentservice.infrastructure.client.pg.TossPaymentClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentProviderAdaptor implements PaymentProvider {

    private final TossPaymentClient tossPaymentClient;

    @Override
    public PaymentProviderConfirmResult confirm(String paymentKey, String orderId, int amount) {
        TossConfirmRequest requestBody = new TossConfirmRequest(paymentKey, orderId, amount);

        TossConfirmResponse response = tossPaymentClient.confirm(requestBody);
        log.info("[Toss 결제 승인 response={}", response);
        return response.toResult();
    }

    @Override
    public void cancel(String paymentKey) {

    }
}
