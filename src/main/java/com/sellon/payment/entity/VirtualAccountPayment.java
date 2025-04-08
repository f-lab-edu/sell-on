package com.sellon.payment.entity;

import com.sellon.order.entity.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("VIRTUAL_ACCOUNT")
public class VirtualAccountPayment extends Payment {
    private String accountNumber;
    private String bankName;
    private String expDate;

    public VirtualAccountPayment(
        String transactionId,
        BigDecimal amount,
        Order order,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        String accountNumber,
        String bankName,
        String expDate
    ) {
        super(transactionId, amount, order, paymentMethod, paymentStatus);
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.expDate = expDate;
    }
}
