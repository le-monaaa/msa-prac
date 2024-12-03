package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.domain.AddProductRequest;
import com.sparta.msa_exam.product.domain.ProductResponse;
import com.sparta.msa_exam.product.domain.Product;
import com.sparta.msa_exam.product.domain.ResponseDto;
import com.sparta.msa_exam.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

    // product Id 유효성 체크
    public ResponseDto validateProductIds(List<Long> productIds) {
        // id 리스트에 해당하는 상품 조회
        List<Long> existProductIds = productRepository.findAllById(productIds)
                .stream()
                .map(Product::getId)
                .toList();
        // 존재하지 않는 id 체크
        List<Long> invalidIds = productIds.stream()
                .filter(productId -> !existProductIds.contains(productId))
                .toList();
        if(!invalidIds.isEmpty()) {
            return new ResponseDto(ResponseDto.FAILURE, "유효하지 않은 상품번호", null);
        }
        return new ResponseDto(ResponseDto.SUCCESS, "", null);
    }

    // productId 단건 유효성 체크
    public ResponseDto validateProductId(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if(product == null) {
            return new ResponseDto(ResponseDto.FAILURE, "유효하지 않은 상품입니다", null);
        }
        return new ResponseDto(ResponseDto.SUCCESS, "상품 조회 성공", null);
    }
}
