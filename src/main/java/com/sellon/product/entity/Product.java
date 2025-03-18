package com.sellon.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 상품 Id

    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private LocalDateTime createdAt;

    public void updateStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }

}
