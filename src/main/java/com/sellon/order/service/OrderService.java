package com.sellon.order.service;

import com.sellon.order.dto.OrderItemResponse;
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
        String orderNumber = generatedOrderNumber();

        Order order = orderMapper.toEntity(orderRequest, orderNumber);

        if (orderRequest.getOrderItems() != null && !orderRequest.getOrderItems().isEmpty()) {
            orderRequest.getOrderItems().forEach(orderItemRequest -> {
                Product product = productRepository.findById(orderItemRequest.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("상품을 찾울 수 없습니다."));

                OrderItem orderItem = orderItemMapper.toEntity(orderItemRequest, product);

                order.addOrderItem(orderItem);
            });
        }

        return orderRepository.save(order);
    }

    private String generatedOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
