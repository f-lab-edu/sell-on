package com.sellon.payment.entity;

import com.sellon.order.entity.Order;
import com.sellon.payment.exception.PaymentProcessingException;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "payment_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 결제 수단

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태

    private LocalDateTime paymentDate;
    private LocalDateTime canceledDate;
    private String failReason;

    // 외부 결제 시스템 연동 정보
    private String externalPaymentKey;

    public Payment(
            String transactionId,
            BigDecimal amount,
            Order order,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus
    ) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = LocalDateTime.now();
    }

    public void complete(LocalDateTime completionTime) {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new PaymentProcessingException("대기 중인 결제만 완료 처리 가능합니다.");
        }
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paymentDate = completionTime;
    }

    public void fail(String reason) {
        if (this.paymentStatus == PaymentStatus.COMPLETED || this.paymentStatus == PaymentStatus.CANCELED) {
            throw new IllegalStateException("완료되거나 취소된 결제는 실패로 변경할 수 없습니다.");
        }
        this.paymentStatus = PaymentStatus.FAILED;
        this.failReason = reason;
    }

    public void cancel(LocalDateTime cancellationTime) {
        this.paymentStatus = PaymentStatus.CANCELED;
        this.canceledDate = cancellationTime;
    }

    public void setExternalPaymentKey(String externalPaymentKey) {
        this.externalPaymentKey = externalPaymentKey;
    }

}
