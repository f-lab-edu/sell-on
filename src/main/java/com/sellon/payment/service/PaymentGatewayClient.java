package com.sellon.payment.service;

import com.sellon.payment.dto.examplePG.Card;
import com.sellon.payment.dto.examplePG.ExamplePgRequest;
import com.sellon.payment.dto.examplePG.ExamplePgResponse;
import com.sellon.payment.dto.examplePG.VirtualAccount;
import com.sellon.payment.entity.Payment;
import org.springframework.stereotype.Component;

/**
 * 다양한 PG사에서 공통적으로 구현할 수 있는 결제 게이트웨이 클라이언트 인터페이스
 * 지원 결제 수단: 카드, 가상계좌, 무통장 입금
 */
@Component
public interface PaymentGatewayClient {
    ExamplePgResponse<Card> requestCardPayment(ExamplePgRequest request);
    ExamplePgResponse<VirtualAccount> issueVirtualAccount(ExamplePgRequest request);
}
