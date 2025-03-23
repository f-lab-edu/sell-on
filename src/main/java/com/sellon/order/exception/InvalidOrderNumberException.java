package com.sellon.order.exception;

public class InvalidOrderNumberException extends OrderProcessingException {
    public InvalidOrderNumberException(String message) {
        super(message);
    }
}
