package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.domain.AddProductRequest;
import com.sparta.msa_exam.product.domain.ProductResponse;
import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    public ProductResponse addProduct(AddProductRequest request) {
        Product prodcut = productRepository.save(new Product().builder()
                .name(request.getName())
                .supplyPrice(request.getSupplyPrice())
                .build());
        return ProductResponse.builder()
                .productId(prodcut.getId())
                .name(prodcut.getName())
                .supplyPrice(prodcut.getSupplyPrice())
                .build();
    }

    // 상품 목록 조회
    public List<ProductResponse> getProcuts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getSupplyPrice()
                ))
                .collect(Collectors.toList());
    }
}
