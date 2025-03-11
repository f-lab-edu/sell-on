package com.sellon.product.dto;

import com.sellon.product.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductRequest {
    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "재고는 필수입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stockQuantity;

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();

    }
}
