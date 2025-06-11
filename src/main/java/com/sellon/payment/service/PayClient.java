package com.sellon.payment.service;

import com.sellon.order.entity.Order;
import com.sellon.payment.dto.PayApproveReponse;
import com.sellon.payment.dto.PayApproveRequest;
import com.sellon.payment.dto.PayReadyResponse;
import com.sellon.payment.entity.PaymentMethod;

public interface PayClient {
    boolean isAvailable(PaymentMethod paymentMethod);
    PayReadyResponse ready(Order order);
    PayApproveReponse approve(PayApproveRequest request);
}
