package com.pagely.paymentservice.infrastructure.client.pg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pagely.paymentservice.application.dto.result.PaymentProviderConfirmResult;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TossConfirmResponse(
        String paymentKey,
        String orderId,
        String orderName,
        String status,           // DONE, CANCELED, PARTIAL_CANCELED, ABORTED, EXPIRED
        OffsetDateTime requestedAt,
        OffsetDateTime approvedAt,
        int totalAmount,
        int balanceAmount,
        String method,           // 카드, 가상계좌, 간편결제 등
        Card card
) {
    public PaymentProviderConfirmResult toResult() {
        return new PaymentProviderConfirmResult(
                paymentKey,
                orderId,
                totalAmount,
                method,
                approvedAt != null
                        ? approvedAt.atZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime()
                        : null
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Card(
            String number,
            String company,
            String installmentPlanMonths,
            boolean isInterestFree
    ) {
    }
}
