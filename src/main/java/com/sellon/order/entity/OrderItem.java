package com.sellon.order.entity;

import com.sellon.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String productName; // 상품명 (주문 시점의 상품명)
    private BigDecimal productPrice; // 상품 가격 (주문 시점의 가격)
    private int quantity; // 주문 수량
    private BigDecimal subtotal; // 소계 (가격 * 수량)

    @Builder
    public OrderItem(Product product, String productName,
            BigDecimal productPrice, int quantity, BigDecimal subtotal
    ) {
        this.product = product;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = subtotal != null ? subtotal : calculateSubTotal(productPrice, quantity);
    }

    private BigDecimal calculateSubTotal(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        this.subtotal = calculateSubTotal(this.productPrice, newQuantity);
    }

    public static OrderItem createFrom(Product product, int quantity) {
        return OrderItem.builder()
                .product(product)
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(quantity)
                .build();
    }
}
