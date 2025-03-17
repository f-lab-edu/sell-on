package com.sellon.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("주문 생성"),
    PAID("결제 완료"),
    SHIPPED("배송 중"),
    COMPLETED("배송 완료"),
    CANCELED("주문 취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
