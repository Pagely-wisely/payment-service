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
    private final PaymentProvider paymentProvider;

    public ConfirmPaymentResult confirmPayment(ConfirmPaymentCommand command) {

        paymentCommandService.validateAndMarkConfirmRequested(command);

        // PG 결제 승인 요청
        PaymentProviderConfirmResult pgResult = paymentProvider.confirm(
                command.paymentKey(),
                command.orderId().toString(),
                command.price()
        );

        return paymentCommandService.applyConfirmedResult(command, pgResult);
    }
}
