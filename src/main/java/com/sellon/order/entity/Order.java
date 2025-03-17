package com.sellon.order.entity;

import com.sellon.order.dto.OrderItemRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber; // 주문 번호

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태

    private LocalDateTime orderAt; // 주문 시간
    private LocalDateTime canceledAt; // 취소 시간
    private String cancelReason; // 취소 사유

    private BigDecimal totalAmount; // 총 주문 금액
    private BigDecimal finalAmount; // 최종 결제 금액

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(BigDecimal shippingFee) {
        this.orderNumber = generatedOrderNumber();
        this.status = OrderStatus.CREATED;
        this.orderAt = LocalDateTime.now();
        this.totalAmount = BigDecimal.ZERO;
        this.finalAmount = BigDecimal.ZERO;
    }

    private String generatedOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // 주문 항목 추가
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        calculateTotalAmount();
    }

    // 여러 주문 항목 추가
    public void addOrderItems(List<OrderItem> items) {
        this.orderItems.addAll(items);
        calculateTotalAmount();
    }

    //주문 취소
    public void cancel(String reason) {
        this.status = OrderStatus.CANCELED;
        this.cancelReason = reason;
        this.canceledAt = LocalDateTime.now();
    }

    // 총 금액 계산
    private void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // 주문 상태 변경
    public void changeStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    // 팩토리 메서드 - 주문 생성
    public static Order createOrder(List<OrderItemRequest> itemRequests) {
        Order order = Order.builder().build();

        for (OrderItemRequest request : itemRequests) {
            OrderItem orderItem = OrderItem.createFrom(
                    request.getProduct(),
                    request.getQuantity()
            );

            order.addOrderItem(orderItem);
        }

        return order;
    }

}
