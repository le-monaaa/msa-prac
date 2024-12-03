package com.sparta.msa_exam.order.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateOrderRequest {

    @NotBlank(message = "상품이 없습니다")
    private List<Long> productIds;
}
