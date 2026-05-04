package com.pagely.paymentservice.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.paymentservice.application.dto.command.CreatePaymentCommand;
import com.pagely.paymentservice.application.service.PaymentCommandService;
import com.pagely.paymentservice.infrastructure.messaging.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderCreatedConsumer {

    private final PaymentCommandService paymentCommandService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-service"
    )
    public void handleOrderCreated(String strEvent) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(strEvent, OrderCreatedEvent.class);
            log.info("[payment] order-created 이벤트 수신: {}", event.domainId());

            CreatePaymentCommand command = new CreatePaymentCommand(
                    event.payload().orderId(),
                    event.payload().buyerId(),
                    event.payload().price(),
                    event.payload().sellerId()
            );

            paymentCommandService.createPayment(command);
        } catch (JsonProcessingException e) {
            log.error("[payment] order-created 이벤트 역직렬화 실패. message={}", strEvent, e);
            //TODO: 실패 재처리 로직 필요
        } catch (Exception e) {
            log.error("[payment] order-created 처리 실패. message={}", strEvent, e);
            //TODO: 실패 재처리 로직 필요
        }
    }
}
