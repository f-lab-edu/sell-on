package com.sellon.payment.exception;

public class PaymentNotFoundException extends PaymentProcessingException {
    public PaymentNotFoundException(String message) {
        super(message);
    }
}
