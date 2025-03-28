package com.sellon.payment.service;

import com.sellon.order.entity.Order;
import com.sellon.payment.entity.*;
import com.sellon.payment.exception.PaymentNotFoundException;
import com.sellon.payment.exception.PaymentProcessingException;
import com.sellon.payment.repository.PaymentLogRepository;
import com.sellon.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentLogRepository paymentLogRepository;
    private final PaymentGatewayClient paymentGatewayClient;

    @Transactional
    public Payment preparePayment(Order order, PaymentMethod method) {
        String transactionId = generateUniqueTransactionId();

        Payment payment = new Payment(
                transactionId,
                order.getFinalAmount(),
                method,
                PaymentStatus.PENDING
        );

        // 외부 결제 시스템 연동 로직
        String externalPaymentKey = paymentGatewayClient.initiatePayment(payment);
        payment.setExternalPaymentKey(externalPaymentKey);

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment approvePayment(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentNotFoundException("정산(Payment)을 찾을 수 없습니다.: " + transactionId));

        boolean isApproved = paymentGatewayClient.verifyPayment(payment);

        if (isApproved) {
            payment.complete(LocalDateTime.now());
            PaymentLog log = PaymentLog.create(payment, PaymentStatus.PENDING, PaymentStatus.COMPLETED);
            paymentLogRepository.save(log);
            return paymentRepository.save(payment);
        } else {
            throw new PaymentProcessingException("결제 승인에 실패했습니다.");
        }
    }

    @Transactional
    public Payment cancelPayment(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentProcessingException("정산(Payment)을 찾을 수 없습니다.: " + transactionId));

        boolean isCancelled = paymentGatewayClient.cancelPayment(payment);

        if (isCancelled) {
            payment.cancel(LocalDateTime.now());
            PaymentLog log = PaymentLog.create(payment, payment.getPaymentStatus(), PaymentStatus.CANCELED);
            paymentLogRepository.save(log);
            return paymentRepository.save(payment);
        } else {
            throw new PaymentProcessingException("결제 취소에 실패했습니다.");
        }
    }

    private String generateUniqueTransactionId() {
        return UUID.randomUUID().toString();
    }

}
