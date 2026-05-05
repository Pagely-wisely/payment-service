package com.pagely.paymentservice.domain.repository;

import com.pagely.paymentservice.domain.model.Payment;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);

    Payment findByOrderId(UUID orderId);
}
