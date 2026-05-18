package com.pagely.paymentservice.infrastructure.client.pg.exception;

/**
 * 일시적 장애 — 재시도 시 성공 가능한 기술적 오류. 예) Toss 서버 5xx, 네트워크 타임아웃, PROVIDER_ERROR
 */
public class TossSystemException extends RuntimeException {

    private final String tossCode;

    public TossSystemException(String tossCode, String message) {
        super(message);
        this.tossCode = tossCode;
    }

    public TossSystemException(String message, Throwable cause) {
        super(message, cause);
        this.tossCode = "UNKNOWN";
    }

    public String getTossCode() {
        return tossCode;
    }
}
