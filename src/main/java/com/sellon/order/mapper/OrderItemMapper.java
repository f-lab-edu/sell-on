package com.sellon.order.mapper;

import com.sellon.order.dto.OrderItemRequest;
import com.sellon.order.dto.OrderItemResponse;
import com.sellon.order.entity.OrderItem;
import com.sellon.order.exception.InvalidOrderRequestException;
import com.sellon.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    public OrderItem toEntity(OrderItemRequest orderItemRequest, Product product) {
        if (orderItemRequest == null) {
            throw new InvalidOrderRequestException("주문 항목 요청 정보(OrderItemRequest)가 없습니다.");
        }
        return new OrderItem(
                product,
                orderItemRequest.getQuantity()
        );
    }

    public OrderItemResponse toDto(OrderItem orderItem) {
        if (orderItem == null) {
            throw new InvalidOrderRequestException("주문 항목 정보(OrderItem)가 존재하지 않습니다.");
        }
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getSubtotal()
        );
    }
}
