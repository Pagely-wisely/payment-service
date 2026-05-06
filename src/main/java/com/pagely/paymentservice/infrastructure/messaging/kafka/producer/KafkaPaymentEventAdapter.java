package com.pagely.paymentservice.infrastructure.messaging.kafka.producer;

import com.pagely.paymentservice.application.port.out.PaymentEventPort;
import com.pagely.paymentservice.domain.event.payload.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentEventAdapter implements PaymentEventPort {

    private static final String PAYMENT_COMPLETED_TOPIC = "payment-completed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishPaymentCompleted(PaymentCompletedEvent event) {
        publish(PAYMENT_COMPLETED_TOPIC, event.getDomainId(), event);
    }

    private void publish(String topic, String key, Object event) {
        kafkaTemplate.send(topic, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka 발생 실패 topic: {} key: {}", topic, key);
                    } else {
                        log.info("Kafka 발행 성공 topic: {} key: {} offset: {}",
                                topic, key, result.getRecordMetadata().offset());
                    }
                });
    }
}
