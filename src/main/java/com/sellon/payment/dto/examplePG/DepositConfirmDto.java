package com.sellon.payment.dto.examplePG;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DepositConfirmDto {
    private String transactionId;
    private String orderId;
    private String accountNumber;
    private BigDecimal amount;
}
