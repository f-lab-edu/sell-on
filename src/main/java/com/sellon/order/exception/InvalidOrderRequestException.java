package com.sellon.order.exception;

public class InvalidOrderRequestException extends OrderProcessingException {
    public InvalidOrderRequestException(String message) {
        super(message);
    }
}
