package com.pagely.paymentservice.infrastructure.persistence;

import com.pagely.paymentservice.domain.model.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment, UUID> {
    Payment findByOrderId(UUID orderId);
}
