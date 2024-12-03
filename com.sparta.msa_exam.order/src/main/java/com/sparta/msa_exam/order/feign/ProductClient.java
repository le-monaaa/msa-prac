package com.sparta.msa_exam.order.feign;

import com.sparta.msa_exam.order.domain.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="product", fallback = ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/products/validate")
    ResponseDto validateProducts(@RequestParam("productIds") List<Long> productIds);

    @GetMapping("/products/validate/{productId}")
    ResponseDto validateProduct(@PathVariable("productId") Long productId);
}

