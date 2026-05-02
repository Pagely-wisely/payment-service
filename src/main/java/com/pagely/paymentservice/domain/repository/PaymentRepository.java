package com.pagely.paymentservice.domain.repository;

import com.pagely.paymentservice.domain.model.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
