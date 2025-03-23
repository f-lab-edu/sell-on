package com.sellon.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Order(String orderNumber, OrderStatus status, LocalDateTime orderAt, List<OrderItem> orderItems) {
        this.orderNumber = orderNumber;
        this.status = status;
        this.orderAt = orderAt;
        this.orderItems = orderItems;
        calculateTotalAmount();
        this.finalAmount = this.totalAmount;
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

}
