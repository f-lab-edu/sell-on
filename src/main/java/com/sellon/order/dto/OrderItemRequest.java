package com.sellon.order.dto;

import com.sellon.product.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemRequest {
    private Product product;
    private int quantity;
}
