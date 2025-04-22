package com.sellon.payment.service;

import com.sellon.order.entity.Order;
import com.sellon.order.exception.OrderNotFoundException;
import com.sellon.order.repository.OrderRepository;
import com.sellon.payment.dto.examplePG.Card;
import com.sellon.payment.dto.examplePG.DepositConfirmDto;
import com.sellon.payment.dto.examplePG.ExamplePgRequest;
import com.sellon.payment.dto.examplePG.ExamplePgResponse;
import com.sellon.payment.dto.examplePG.VirtualAccount;
import com.sellon.payment.entity.*;
import com.sellon.payment.exception.PaymentFailedException;
import com.sellon.payment.exception.PaymentNotFoundException;
import com.sellon.payment.exception.PaymentProcessingException;
import com.sellon.payment.mapper.ExamplePaymentMapper;
import com.sellon.payment.repository.PaymentLogRepository;
import com.sellon.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceTemp {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final ExamplePaymentMapper mapper;
    private final PaymentGatewayClient paymentGatewayClient;

    /**
     * 카드 결제 처리
     */
    @Transactional
    public Payment processCardPayment(ExamplePgRequest request) {
        ExamplePgResponse<Card> response = paymentGatewayClient.requestCardPayment(request);

        if (response.isSuccess()) {
            Order order = orderRepository.findByOrderNumber(request.getOrderNumber())
                    .orElseThrow(() -> new OrderNotFoundException("주문을 찾을 수 없습니다."));

            CardPayment payment = mapper.toCardPayment(response, order);

            return confirmDeposit(payment);
        } else {
            throw new PaymentFailedException("카드 결제 처리 실패");
        }
    }

    /**
     * 가상계좌 정산 발급
     */
    @Transactional
    public VirtualAccountPayment processVirtualAccountPayment(ExamplePgRequest request) {
        ExamplePgResponse<VirtualAccount> response = paymentGatewayClient.issueVirtualAccount(request);

        if (response.isSuccess()) {
            Order order = orderRepository.findByOrderNumber(request.getOrderNumber())
                    .orElseThrow(() -> new PaymentNotFoundException("Order not found"));

            VirtualAccountPayment payment = mapper.toVirtualAccount(response, order);

            return paymentRepository.save(payment);
        } else {
            throw new PaymentFailedException("가상계좌 발급 실패");
        }
    }

    /**
     * 무통장 정산 발급
     */
    @Transactional
    public BankTransferPayment processBankTransferPayment(Order order) {
        String transactionId = generateUniqueTransactionId();

        BankTransferPayment payment = new BankTransferPayment(
                transactionId,
                order.getTotalAmount(),
                order,
                PaymentMethod.BANK_TRANSFER,
                PaymentStatus.PENDING
        );

        return paymentRepository.save(payment);
    }

    /**
     * 가상계좌 입금 확인
     */
    @Transactional
    public Payment receiveDepositCallback(DepositConfirmDto depositConfirmDto) {
        String transactionId = depositConfirmDto.getTransactionId();

        VirtualAccountPayment payment = paymentRepository.findByTransactionId(transactionId)
                .map(p -> (VirtualAccountPayment) p)
                .orElseThrow(() -> new PaymentNotFoundException("해당 계좌번호의 결제정보를 찾을 수 없습니다: " + transactionId));

        if (payment.getAmount() != depositConfirmDto.getAmount()) {
            throw new PaymentProcessingException("입금액이 결제 금액과 일치하지 않습니다.");
        }

        return confirmDeposit(payment);
    }

    /**
     * 무통장 입금 확인
     */
    @Transactional
    public Payment confirmByAdmin(String transactionId, String adminId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException("해당 트랜잭션 아이디의 결제정보를 찾을 수 없습니다: " + transactionId));
        +
        return confirmDeposit(payment);
    }

    /**
     * 입금 확인 처리
     */
    @Transactional
    public Payment confirmDeposit(Payment payment) {
        payment.complete(LocalDateTime.now());
        PaymentLog log = PaymentLog.create(payment, PaymentStatus.PENDING, PaymentStatus.COMPLETED);
        paymentLogRepository.save(log);

        return paymentRepository.save(payment);
    }

    private String generateUniqueTransactionId() {
        return UUID.randomUUID().toString();
    }

}
