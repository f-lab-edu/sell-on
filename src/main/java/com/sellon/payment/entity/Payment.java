package com.sellon.payment.entity;

import com.sellon.order.entity.Order;
import com.sellon.payment.exception.PaymentProcessingException;
import jakarta.persistence.Column;
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
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; // 결제 수단

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // 결제 상태

    private LocalDateTime paymentDate;
    private LocalDateTime canceledDate;
    private String failReason;

    // 외부 결제 시스템 연동 정보
    private String externalPaymentKey;

    @Builder
    public Payment(
        BigDecimal amount,
        Order order,
        PaymentMethod paymentMethod
    ) {
        this.amount = amount;
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = PaymentStatus.PENDING;
        this.paymentDate = LocalDateTime.now();
    }

    private static final Map<PaymentStatus, Set<PaymentStatus>> POSSIBLE_STATUS = Map.of(
        PaymentStatus.PENDING,
        Set.of(PaymentStatus.COMPLETED, PaymentStatus.FAILED, PaymentStatus.CANCELED),
        PaymentStatus.COMPLETED, Set.of(PaymentStatus.REFUNDED),
        PaymentStatus.FAILED, Set.of(PaymentStatus.PENDING, PaymentStatus.CANCELED),
        PaymentStatus.CANCELED, Set.of(),
        PaymentStatus.REFUNDED, Set.of()
    );

    public boolean canTransitionTo(PaymentStatus targetStatus) {
        Set<PaymentStatus> allowedStatuses = POSSIBLE_STATUS.get(this.paymentStatus);
        return allowedStatuses != null && allowedStatuses.contains(targetStatus);
    }

    private void validateTransition(PaymentStatus targetStatus) {
        if (!canTransitionTo(targetStatus)) {
            throw new PaymentProcessingException(
                String.format("결제 상태를 %s에서 %s로 변경할 수 없습니다. 허용된 상태: %s",
                    paymentStatus,
                    targetStatus,
                    POSSIBLE_STATUS.get(paymentStatus))
            );
        }
    }

    public Payment complete(LocalDateTime completionTime) {
        validateTransition(PaymentStatus.COMPLETED);

        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paymentDate = completionTime;
        return this;
    }

    public Payment fail(String reason) {
        validateTransition(PaymentStatus.FAILED);

        this.paymentStatus = PaymentStatus.FAILED;
        this.failReason = reason;
        return this;
    }

    public Payment cancel() {
        validateTransition(PaymentStatus.CANCELED);

        this.paymentStatus = PaymentStatus.CANCELED;
        this.canceledDate = LocalDateTime.now();
        return this;
    }

    public Payment refund(LocalDateTime refundTime) {
        validateTransition(PaymentStatus.REFUNDED);

        this.paymentStatus = PaymentStatus.REFUNDED;
        this.paymentDate = refundTime;
        return this;
    }

    public void setExternalPaymentKey(String externalPaymentKey) {
        this.externalPaymentKey = externalPaymentKey;
    }

}
