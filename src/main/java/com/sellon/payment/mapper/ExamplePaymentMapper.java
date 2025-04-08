package com.sellon.payment.mapper;

import com.sellon.order.entity.Order;
import com.sellon.payment.dto.examplePG.Card;
import com.sellon.payment.dto.examplePG.ExamplePgResponse;
import com.sellon.payment.dto.examplePG.PaymentData;
import com.sellon.payment.dto.examplePG.VirtualAccount;
import com.sellon.payment.entity.CardPayment;
import com.sellon.payment.entity.PaymentMethod;
import com.sellon.payment.entity.PaymentStatus;
import com.sellon.payment.entity.VirtualAccountPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamplePaymentMapper {
    /**
     * ExamplePG 카드 결제 응답을 CardPayment 엔티티로 변환
     */
    public CardPayment toCardPayment(ExamplePgResponse<Card> response, Order order) {
        if (response == null || !response.isSuccess()) {
            throw new IllegalArgumentException("Invalid response: " + response);
        }

        PaymentData<Card> paymentData = response.getData();
        Card cardData = paymentData.getCard();

        return new CardPayment(
            paymentData.getTransactionId(),
            paymentData.getAmount(),
            order,
            PaymentMethod.CARD,
            PaymentStatus.COMPLETED,
            cardData.getCode(),
            cardData.getName(),
            cardData.getNumber()
        );
    }

    /**
     * ExamplePG 가상계좌 결제 응답을 VirtualAccountPayment 엔티티로 변환
     */
    public VirtualAccountPayment toVirtualAccount(ExamplePgResponse<VirtualAccount> response, Order order) {
        if (response == null || !response.isSuccess()) {
            throw new IllegalArgumentException("Invalid response: " + response);
        }

        PaymentData<VirtualAccount> paymentData = response.getData();
        VirtualAccount virtualAccount = paymentData.getVirtualAccount();

        return new VirtualAccountPayment(
            paymentData.getTransactionId(),
            paymentData.getAmount(),
            order,
            PaymentMethod.CARD,
            PaymentStatus.PENDING,
            virtualAccount.getAccountNumber(),
            virtualAccount.getBankName(),
            virtualAccount.getExpDate()
        );
    }

}
