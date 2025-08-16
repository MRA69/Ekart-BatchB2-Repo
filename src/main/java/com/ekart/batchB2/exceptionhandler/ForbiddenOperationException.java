package com.ekart.batchB2.exceptionhandler;

public class ForbiddenOperationException extends Exception {
    private static final long serialVersionUID = 1L;

    public ForbiddenOperationException() {
        super();
    }

    public ForbiddenOperationException(String message) {
        super(message);
    }
}
