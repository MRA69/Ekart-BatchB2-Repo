package com.ekart.batchB2.exceptionhandler;

public class DuplicateCategoryException extends Exception {
    private static final long serialVersionUID = 1L;

    public DuplicateCategoryException() {
        super();
    }

    public DuplicateCategoryException(String message) {
        super(message);
    }
}
