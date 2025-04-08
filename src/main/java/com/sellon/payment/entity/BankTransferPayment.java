package com.sellon.payment.entity;

import com.sellon.order.entity.Order;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("BANK_TRANSFER")
@NoArgsConstructor
public class BankTransferPayment extends Payment {

    public BankTransferPayment(
        String transactionId,
        BigDecimal amount,
        Order order,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus
    ) {
        super(transactionId, amount, order, paymentMethod, paymentStatus);
    }
}
