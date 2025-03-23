package com.sellon.order.mapper;

import com.sellon.order.dto.OrderItemResponse;
import com.sellon.order.dto.OrderRequest;
import com.sellon.order.dto.OrderResponse;
import com.sellon.order.entity.Order;
import com.sellon.order.entity.OrderStatus;
import com.sellon.order.exception.InvalidOrderRequestException;
import com.sellon.order.exception.InvalidOrderNumberException;
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

    public Order toEntity(OrderRequest orderRequest, String orderNumber) {
        if (orderRequest == null) {
            throw new InvalidOrderRequestException("주문 요청 정보(OrderRequest)가 없습니다.");
        }

        if (orderNumber == null || orderNumber.isBlank()) {
            throw new InvalidOrderNumberException("주문 번호(OrderNumber)가 유효하지 않습니다.");
        }

        LocalDateTime now = LocalDateTime.now();

        Order order = new Order(
                orderNumber,
                OrderStatus.CREATED,
                now,
                new ArrayList<>()
        );

        return order;
    }

    public OrderResponse toDto(Order order) {
        if (order == null) {
            throw new InvalidOrderRequestException("주문 정보(Order)가 존재하지 않습니다.");
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
