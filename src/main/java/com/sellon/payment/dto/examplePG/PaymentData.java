package com.sellon.payment.dto.examplePG;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentData<T> {
    private String merchantId;      // 상점 ID
    private String transactionId;   // 거래 ID
    private String orderNumber;     // 주문 번호
    private String payMethod;
    private BigDecimal amount;
    private Card card;
    private VirtualAccount virtualAccount;
    private Buyer buyer;
    private Goods goods;
    private T detail;
}
