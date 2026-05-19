package com.pagely.paymentservice.application.exception;

public class PgAlreadyProcessedException extends RuntimeException {

    public PgAlreadyProcessedException(String message) {
        super(message);
    }
}
