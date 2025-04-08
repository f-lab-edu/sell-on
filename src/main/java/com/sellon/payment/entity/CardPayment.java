package com.sellon.payment.entity;

import com.sellon.order.entity.Order;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@DiscriminatorValue("CARD")
public class CardPayment extends Payment {
    private String code;
    private String Name;
    private String number;

    public CardPayment(
        String transactionId,
        BigDecimal amount,
        Order order,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        String code,
        String Name,
        String number
    ) {
        super(transactionId, amount, order, paymentMethod, paymentStatus);
        this.code = code;
        this.Name = Name;
        this.number = number;
    }

}
