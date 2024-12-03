package com.sparta.msa_exam.product.controller;

import com.sparta.msa_exam.product.domain.AddProductRequest;
import com.sparta.msa_exam.product.domain.ProductResponse;
import com.sparta.msa_exam.product.domain.ResponseDto;
import com.sparta.msa_exam.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    // 상품 추가
    @PostMapping
    public ResponseDto addProduct(@Valid @RequestBody AddProductRequest request) {
        ProductResponse response = productService.addProduct(request);

        return new ResponseDto(ResponseDto.SUCCESS, "상품 등록 성공", response);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseDto getProducts() {
        List<ProductResponse> products = productService.getProcuts();

        if(products == null) {
            return new ResponseDto(ResponseDto.SUCCESS, "상품이 없습니다", null);
        }
        return new ResponseDto(ResponseDto.SUCCESS, null, products);
    }

    // product ids 유효성 체크
    @GetMapping("/validate")
    public ResponseDto validateProductIds(@RequestParam List<Long> productIds) {
        return productService.validateProductIds(productIds);
    }

    // product id 단건 유효성 체크
    @GetMapping("/validate/{id}")
    public ResponseDto validateProductId(@PathVariable Long id) {
        return productService.validateProductId(id);
    }


}
