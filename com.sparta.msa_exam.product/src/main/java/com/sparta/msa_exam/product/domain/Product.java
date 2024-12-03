package com.sparta.msa_exam.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotBlank(message = "상품 이름 없음")
    @Size(max = 50, message = "상품 이름: 50자 제한")
    private String name;

    @PositiveOrZero(message = "가격은 0 이상이어야 함")
    private Integer supplyPrice;

}
