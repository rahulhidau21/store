package com.example.store.exception;

public class BadRequestException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public final String message;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    public BadRequestException(String message, Throwable t) {
        super(message, t);
        this.message = message;
    }
}
