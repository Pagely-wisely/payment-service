package com.pagely.paymentservice.presentation.dto.request;

import com.pagely.paymentservice.application.dto.command.ConfirmPaymentCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ConfirmPaymentRequest(
        @NotBlank(message = "paymentKey는 필수입니다.")
        String paymentKey,

        @NotNull(message = "orderId는 필수입니다.")
        UUID orderId,

        @NotNull(message = "amount는 필수입니다.")
        @Min(value = 0, message = "amount는 0 이상이어야 합니다.")
        Integer amount
) {
    public ConfirmPaymentCommand toCommand(UUID userId) {
        return new ConfirmPaymentCommand(
                this.paymentKey,
                this.orderId,
                this.amount,
                userId
        );
    }
}
