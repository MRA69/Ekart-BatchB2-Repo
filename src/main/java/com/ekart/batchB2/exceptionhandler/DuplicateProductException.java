package com.ekart.batchB2.exceptionhandler;

public class DuplicateProductException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateProductException() {
        super();
    }

    public DuplicateProductException(String message) {
        super(message);
    }
}
