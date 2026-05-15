package com.pagely.paymentservice.application.service;

import com.pagely.paymentservice.application.dto.result.ConfirmPaymentResult;
import com.pagely.paymentservice.domain.model.Payment;
import com.pagely.paymentservice.domain.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryService {

    private final PaymentRepository paymentRepository;

    // 이미 완료된 결제인지 조회
    public ConfirmPaymentResult findIfAlreadyCompleted(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .filter(Payment::isCompleted)
                .map(ConfirmPaymentResult::fromEntity)
                .orElse(null);
    }
}
