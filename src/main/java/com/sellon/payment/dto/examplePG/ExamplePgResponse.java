package com.sellon.payment.dto.examplePG;

import lombok.Data;

@Data
public class ExamplePgResponse<T> {
    private boolean success;
    private PaymentData<T> data;
}
