package com.sellon.product.service;

import com.sellon.product.dto.ProductRequest;
import com.sellon.product.dto.ProductResponse;
import com.sellon.product.entity.Product;
import com.sellon.product.mapper.ProductMapper;
import com.sellon.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    // 상품 등록
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productRepository.save(mapper.toEntity(request));
        return mapper.toDto(product);
    }

    // 상품 전체 조회
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // 특정 상품 조회
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다. ID: " + id));
        return mapper.toDto(product);
    }

}
