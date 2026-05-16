package com.pagely.paymentservice.presentation.controller;

import com.pagely.common.auth.Role;
import com.pagely.common.auth.annotation.AuthRequired;
import com.pagely.common.auth.annotation.CurrentUserId;
import com.pagely.common.response.ApiResponse;
import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.application.service.PaymentCommandFacade;
import com.pagely.paymentservice.presentation.dto.request.ConfirmPaymentRequest;
import com.pagely.paymentservice.presentation.dto.response.ConfirmPaymentResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentCommandFacade paymentCommandFacade;

    @PostMapping("/confirm")
    @AuthRequired(role = Role.USER)
    public ResponseEntity<ApiResponse> confirmPayment(
            @CurrentUserId UUID userId,
            @Valid @RequestBody ConfirmPaymentRequest request
    ) {
        ConfirmPaymentResult result = paymentCommandFacade.confirmPayment(request.toCommand(userId));
        return ApiResponse.ok(ConfirmPaymentResponse.fromResult(result));
    }
}
