package com.pagely.paymentservice.infrastructure.client.pg.exception;

/**
 * Toss 기준으로 이미 승인 완료된 결제인데 우리 DB에 반영이 안 된 경우. markAsFailed()가 아닌 Toss 결제 조회 API로 상태를 동기화해야 한다.
 */
public class TossAlreadyProcessedException extends RuntimeException {

    public TossAlreadyProcessedException(String message) {
        super(message);
    }
}
