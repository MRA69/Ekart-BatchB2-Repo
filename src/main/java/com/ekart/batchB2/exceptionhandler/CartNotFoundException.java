package com.ekart.batchB2.exceptionhandler;

public class CartNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public CartNotFoundException() {
        super();
    }
    public CartNotFoundException(String message) {
        super(message);
    }
}
