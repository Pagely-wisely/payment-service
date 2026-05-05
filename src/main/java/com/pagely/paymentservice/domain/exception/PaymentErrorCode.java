package com.pagely.paymentservice.domain.exception;

import com.pagely.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_BUYER_MISMATCH("결제자 정보가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    PAYMENT_AMOUNT_MISMATCH("결제 금액 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_ORDER_ID_MISMATCH("결제 주문 ID가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    PAYMENT_NOT_FOUND("해당 결제정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PG_CONFIRM_FAILED("PG 결제 승인에 실패하였습니다.", HttpStatus.BAD_GATEWAY);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    PaymentErrorCode(String message, HttpStatus httpStatus) {
        this.code = this.name();
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
