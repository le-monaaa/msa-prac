package com.sparta.msa_exam.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name="orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "order_product_ids", joinColumns = @JoinColumn(name = "order_id"))
    private List<Long> productIds;


}
