package com.sparta.msa_exam.product.domain;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponse {

    private Long productId;
    private String name;
    private Integer supplyPrice;
}
