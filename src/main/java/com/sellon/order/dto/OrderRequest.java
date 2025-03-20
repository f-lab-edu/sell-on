package com.sellon.order.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {
    @NotEmpty(message = "주문 항목 목록은 비어 있을 수 없습니다.")
    private List<OrderItemRequest> orderItems;
}
