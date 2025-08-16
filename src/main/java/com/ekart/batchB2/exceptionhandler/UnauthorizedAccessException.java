package com.ekart.batchB2.exceptionhandler;

public class UnauthorizedAccessException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnauthorizedAccessException() {
        super();
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
