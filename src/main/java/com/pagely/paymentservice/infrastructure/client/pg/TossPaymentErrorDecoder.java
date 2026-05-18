package com.pagely.paymentservice.infrastructure.client.pg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.paymentservice.application.exception.PgAlreadyProcessedException;
import com.pagely.paymentservice.application.exception.PgRejectedException;
import com.pagely.paymentservice.application.exception.PgResponseParseException;
import com.pagely.paymentservice.application.exception.PgSystemException;
import com.pagely.paymentservice.infrastructure.client.pg.dto.TossErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TossPaymentErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        TossErrorResponse error = parseBody(response);
        if (error == null) {
            log.error("[Toss] 응답 바디 파싱 실패. status={}", response.status());
            return new PgResponseParseException("Toss 오류 응답 파싱 실패");
        }

        log.warn("[Toss] API 오류. code={}, message={}, status={}",
                error.code(), error.message(), response.status());

        // 이미 결제되어 DB 동기화 작업 필요한 경우
        if ("ALREADY_PROCESSED_PAYMENT".equals(error.code())) {
            return new PgAlreadyProcessedException(error.message());
        }

        // 500대 에러 재시도 필요
        if (response.status() >= 500) {
            return new PgSystemException(error.code(), error.message());
        }

        // 400대 에러는 비즈니스 거절이므로 재시도 X
        return new PgRejectedException(error.code(), error.message());
    }

    private TossErrorResponse parseBody(Response response) {
        if (response.body() == null) {
            return null;
        }
        try {
            String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return objectMapper.readValue(body, TossErrorResponse.class);
        } catch (IOException e) {
            log.warn("[Toss] 응답 바디 파싱 중 예외 발생", e);
            return null;
        }
    }
}
