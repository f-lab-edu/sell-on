package com.sellon.order.service;

import com.sellon.order.dto.OrderItemRequest;
import com.sellon.order.dto.OrderRequest;
import com.sellon.order.entity.Order;
import com.sellon.order.entity.OrderItem;
import com.sellon.order.mapper.OrderItemMapper;
import com.sellon.order.mapper.OrderMapper;
import com.sellon.order.repository.OrderRepository;
import com.sellon.product.entity.Product;
import com.sellon.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public Order createOrder(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);
        String orderNumber = generatedOrderNumber();
        Order order = orderMapper.toEntity(orderRequest, orderNumber);

        addOrderItems(order, orderRequest);

        return orderRepository.save(order);
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("주문 요청이 비어 있습니다.");
        }

        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }
    }

    private void addOrderItems(Order order, OrderRequest orderRequest) {
        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {
            Product product = findProductById(orderItemRequest);
            OrderItem orderItem = orderItemMapper.toEntity(orderItemRequest, product);
            order.addOrderItem(orderItem);
        }
    }

    private Product findProductById(OrderItemRequest orderItemRequest) {
        return productRepository.findById(orderItemRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    private String generatedOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
