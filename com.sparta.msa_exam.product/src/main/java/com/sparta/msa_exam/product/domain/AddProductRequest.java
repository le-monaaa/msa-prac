package com.sparta.msa_exam.product.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductRequest {

    @NotBlank(message = "상품명이 입력되지 않았습니다")
    @Size(max=50, message = "상품명은 50자 이내여야합니다")
    private String name;
    @PositiveOrZero(message = "상품 가격은 0원 이상이어야 합니다")
    private Integer supplyPrice;
}
