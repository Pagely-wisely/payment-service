package com.pagely.paymentservice.infrastructure.client.pg;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TossPaymentFeignConfig {

    @Value("${toss.secret-key}")
    private String secretKey;

    @Bean
    public RequestInterceptor tossAuthInterceptor() {
        return requestTemplate -> {
            String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
            requestTemplate.header("Authorization", "Basic " + encoded);
        };
    }

    @Bean
    public ErrorDecoder tossPaymentErrorDecoder(ObjectMapper objectMapper) {
        return new TossPaymentErrorDecoder(objectMapper);
    }
}
