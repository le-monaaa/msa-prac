package com.sparta.msa_exam.order.feign;

import com.sparta.msa_exam.order.domain.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ResponseDto validateProducts(List<Long> productIds) {
        log.error("Fallback triggered for validateProducts with productIds: {}", productIds);
        throw new RuntimeException("Product Service fallback for productIds: " + productIds);
    }

    @Override
    public ResponseDto validateProduct(Long productId) {
        log.error("Fallback triggered for validateProducts with productId: {}", productId);
        throw new RuntimeException("Product Service fallback for productId: " + productId);
    }
}
