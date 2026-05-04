package com.pagely.paymentservice.application.port.out;

import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;

public interface PaymentProvider {

    PaymentProviderConfirmResult confirm(String paymentKey, String orderId, int price);

    void cancel(String paymentKey);
}
