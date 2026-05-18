package com.pagely.paymentservice.application.exception;

public class PgSystemException extends RuntimeException {

    private final String pgCode;

    public PgSystemException(String pgCode, String message) {
        super(message);
        this.pgCode = pgCode;
    }

    public PgSystemException(String message, Throwable cause) {
        super(message, cause);
        this.pgCode = "UNKNOWN";
    }

    public String getPgCode() {
        return pgCode;
    }

}
