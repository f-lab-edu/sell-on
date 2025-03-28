package com.sellon.payment.entity;

import org.springframework.stereotype.Component;

/**
 * 결제 게이트웨이 클라이언트 인터페이스
 */
@Component
public interface PaymentGatewayClient {
    // 결제 초기화
    String initiatePayment(Payment payment);

    // 결제 검증
    boolean verifyPayment(Payment payment);

    // 결제 취소
    boolean cancelPayment(Payment payment);
}
