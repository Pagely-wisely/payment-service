package com.pagely.paymentservice.infrastructure.messaging.event;

import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        String eventId,
        String eventType,
        String domainType,
        String domainId,
        Instant occurredAt,
        Payload payload
) {
    public record Payload(
            UUID orderId,
            UUID buyerId,
            int price,
            UUID sellerId
    ) {
    }
}
