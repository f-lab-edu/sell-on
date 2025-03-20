package com.sellon.product.mapper;

import com.sellon.product.dto.ProductRequest;
import com.sellon.product.dto.ProductResponse;
import com.sellon.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    // ProductRequest -> Product
    @Mapping(target = "id", ignore = true) // ID는 엔티티에서 자동 생성
    @Mapping(target = "createdAt", ignore = true) // CreatedAt은 빌더에서 생성
    Product toEntity(ProductRequest request);

    // Product -> ProductResponse
    ProductResponse toDto(Product product);
}
