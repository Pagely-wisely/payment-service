package com.pagely.paymentservice.infrastructure.client.pg;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "toss",
        url = "${toss.base-url}",
        configuration = TossPaymentFeignConfig.class
)
public interface TossPaymentClient {

    @PostMapping("/v1/payments/confirm")
    TossConfirmResponse confirm(@RequestBody TossConfirmRequest request);
}
