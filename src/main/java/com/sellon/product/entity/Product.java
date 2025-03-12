package com.sellon.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private int price;
    private int stockQuantity;
    private LocalDateTime createdAt;

    @Builder
    public Product(String name, String description, int price, int stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = LocalDateTime.now();
    }

    public void updateStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }

}
