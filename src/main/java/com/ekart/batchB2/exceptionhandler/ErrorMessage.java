package com.ekart.batchB2.exceptionhandler;

public class ErrorMessage {

    private int statusCode;
    private String message;

    public ErrorMessage() {}

    public ErrorMessage(String message, int statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
