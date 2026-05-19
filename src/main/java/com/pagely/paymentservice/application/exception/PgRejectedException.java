package com.pagely.paymentservice.application.exception;

public class PgRejectedException extends RuntimeException {

    private final String pgCode;

    public PgRejectedException(String pgCode, String message) {
        super(message);
        this.pgCode = pgCode;
    }

    public String getPgCode() {
        return pgCode;
    }
}
