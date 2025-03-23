package com.sellon.order.mapper;

import com.sellon.order.dto.OrderItemResponse;
import com.sellon.order.dto.OrderRequest;
import com.sellon.order.dto.OrderResponse;
import com.sellon.order.entity.Order;
import com.sellon.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;

    /**
     * OrderRequest -> Order
     * - 새로운 주문 시 사용
     * @param orderRequest
     * @return
     */
    public Order toEntity(OrderRequest orderRequest, String orderNumber) {
        if (orderRequest == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();

        Order order = new Order(
                null,
                orderNumber,
                OrderStatus.CREATED,
                now,
                null,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new ArrayList<>()
        );

        return order;
    }

    /**
     * Order -> OrderResponse
     * @param order
     * @return
     */
    public OrderResponse toDto(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemResponse> orderItemResponses = order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getOrderAt(),
                order.getCanceledAt(),
                order.getCancelReason(),
                order.getTotalAmount(),
                order.getFinalAmount(),
                orderItemResponses
        );
    }

}
