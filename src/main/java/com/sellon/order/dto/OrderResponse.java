package com.sellon.order.dto;

import com.sellon.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private LocalDateTime orderAt;
    private LocalDateTime canceledAt;
    private String cancelReason;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private List<OrderItemResponse> orderItems;
}
