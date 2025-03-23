package com.sellon.product.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stockQuantity;

    public ProductResponse(Long id, String name, String description, BigDecimal price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}