package com.pagely.paymentservice.application.exception;

public class PgResponseParseException extends RuntimeException {

    public PgResponseParseException(String message) {
        super(message);
    }
}
