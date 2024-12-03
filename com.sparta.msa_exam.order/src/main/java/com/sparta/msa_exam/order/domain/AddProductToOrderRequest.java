package com.sparta.msa_exam.order.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddProductToOrderRequest {

    @NotBlank(message = "상품이 없습니다")
    private Long productId;
}
