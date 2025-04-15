package com.sellon.payment.dto;

import lombok.Data;

@Data
public class PaymentResult {
    private boolean success;
    private String message;

    public PaymentResult(boolean success, String message) {
    }

    public static PaymentResult success(String message) {
        return new PaymentResult(true, message);
    }

    public static PaymentResult failed(String message) {
        return new PaymentResult(false, message);
    }

}
