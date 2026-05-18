package com.pagely.paymentservice.infrastructure.client.pg.exception;

/**
 * 카드사/PG의 명시적 거절 — 재시도해도 결과가 동일한 비즈니스 오류. 예) 카드 한도 초과, 잔액 부족, 정지 카드
 */
public class TossBusinessException extends RuntimeException {

    private final String tossCode;

    public TossBusinessException(String tossCode, String message) {
        super(message);
        this.tossCode = tossCode;
    }

    public String getTossCode() {
        return tossCode;
    }
}
