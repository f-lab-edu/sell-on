package com.sellon.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemResponse {
    private Long id;
    private int quantity;
    private BigDecimal subtotal;
}
