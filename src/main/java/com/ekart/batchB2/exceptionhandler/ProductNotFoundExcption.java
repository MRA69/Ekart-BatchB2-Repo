package com.ekart.batchB2.exceptionhandler;

public class ProductNotFoundExcption extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProductNotFoundExcption() {
        super();
    }
    public ProductNotFoundExcption(String message) {
        super(message);
    }
}
