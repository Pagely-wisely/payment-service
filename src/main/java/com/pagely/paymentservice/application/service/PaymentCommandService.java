package com.pagely.paymentservice.application.service;

import com.pagely.paymentservice.application.dto.command.CreatePaymentCommand;
import com.pagely.paymentservice.domain.model.Payment;
import com.pagely.paymentservice.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentCommandService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void createPayment(CreatePaymentCommand command) {
        Payment payment = Payment.create(command.orderId(), command.buyerId(), command.sellerId(), command.price());
        paymentRepository.save(payment);
    }
}
