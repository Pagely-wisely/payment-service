package com.pagely.paymentservice.infrastructure.persistence;

import com.pagely.paymentservice.domain.model.Payment;
import com.pagely.paymentservice.domain.repository.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;

    @Override
    public Payment save(Payment payment) {
        return jpaPaymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderId(UUID orderId) {
        return jpaPaymentRepository.findByOrderId(orderId);
    }
}
