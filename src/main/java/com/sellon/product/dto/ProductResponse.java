package com.sellon.product.dto;

import com.sellon.product.entity.Product;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        int stockQuantity
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity()
        );
    }
}
