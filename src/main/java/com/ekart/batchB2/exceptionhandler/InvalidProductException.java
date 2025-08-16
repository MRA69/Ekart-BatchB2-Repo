package com.ekart.batchB2.exceptionhandler;

public class InvalidProductException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidProductException() {
        super();
    }
    public InvalidProductException(String message) {
        super(message);
    }
}
