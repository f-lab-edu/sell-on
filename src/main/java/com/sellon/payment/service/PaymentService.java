package com.sellon.payment.service;

import com.sellon.payment.dto.ApprovePaymentRequest;
import com.sellon.payment.dto.ApprovePaymentResponse;
import com.sellon.payment.dto.CreatePaymentRequest;
import com.sellon.payment.dto.CreatePaymentResponse;

public interface PaymentService {
    CreatePaymentResponse createPayment(CreatePaymentRequest request);
    ApprovePaymentResponse approvePayment(ApprovePaymentRequest request);
}
