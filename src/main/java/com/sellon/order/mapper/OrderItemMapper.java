package com.sellon.order.mapper;

import com.sellon.order.dto.OrderItemRequest;
import com.sellon.order.dto.OrderItemResponse;
import com.sellon.order.entity.OrderItem;
import com.sellon.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    public OrderItem toEntity(OrderItemRequest orderItemRequest, Product product) {
        if (orderItemRequest == null) {
            return null;
        }
        return new OrderItem(
                product,
                orderItemRequest.getQuantity()
        );
    }

    public OrderItemResponse toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return new OrderItemResponse(
            orderItem.getId(),
            orderItem.getQuantity(),
            orderItem.getSubtotal()
        );
    }
}
