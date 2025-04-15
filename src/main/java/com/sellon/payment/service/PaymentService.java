package com.sellon.payment.service;

import com.sellon.payment.dto.PaymentResult;
import com.sellon.payment.entity.Payment;
import com.sellon.payment.entity.PaymentMethod;
import com.sellon.payment.entity.PaymentStatus;
import com.sellon.payment.exception.PaymentProcessingException;
import com.sellon.payment.repository.PaymentRepository;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;

    // 결제 트랜잭션 ID별 락 객체를 관리하는 맵
    private final Map<String, Object> paymentLocks = new ConcurrentHashMap<>();

    // 결제 처리 메서드
    public PaymentResult processPayment(Payment payment, PaymentMethod method) {
        // 결제 ID에 해당하는 락 객체 가져오기 (없으면 생성)
        Object lock = paymentLocks.computeIfAbsent(payment.getTransactionId(), k -> new Object());

        // synchronized 블록으로 임계영역 설정
        synchronized (lock) {
            try {
                // 상태체크: 작업 가능한 상태인지 확인
                if (!isValidStateForProcessing(payment.getPaymentStatus())) {
                    throw new PaymentProcessingException("결제 상태가 작업을 허용하지 않습니다.");
                }

                // 결제 수단별 작업 수행
                method.process(payment);

                // 예외가 없으면 성공으로 간주
                updatePaymentStatus(payment, PaymentStatus.COMPLETED);
                return PaymentResult.success("결제 성공");

            } catch (PaymentProcessingException e) {
                // 로깅
                log.error("결제 처리 중 오류 발생: {}", e);
                updatePaymentStatus(payment, PaymentStatus.FAILED);
                return PaymentResult.failed("결제 처리 중 오류 발생: " + e.getMessage());
            } catch (SQLException e) {
                // 디비관련 예외 발생의 경우
                log.error("SQL 예외 발생: {}", e);
                updatePaymentStatus(payment, PaymentStatus.FAILED);
                return PaymentResult.failed("SQL 예외 발생: " + e.getMessage());
            } finally {
                paymentLocks.remove(payment.getTransactionId());
            }
        }

    }
    // 상태 체크 로직
    private boolean isValidStateForProcessing(PaymentStatus status) {
        // INITIALIZED 또는 PROCESSING 상태일 때만 작업 가능
        return status == PaymentStatus.INITIALIZED || status == PaymentStatus.PROCESSING;
    }

    private void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.updateStatus(status);
        paymentRepository.save(payment);
    }


}
