package com.sellon.payment.service.method;

import com.sellon.payment.entity.Payment;

public interface PaymentMethod {
    void process(Payment payment);
}
