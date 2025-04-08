package com.sellon.payment.dto.examplePG;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamplePgRequest {
    private String transactionId;      // 거래 ID
    private String merchantId;         // 가맹점 ID
    private String orderNumber;        // 주문 번호
    private BigDecimal amount;         // 결제 금액
}
