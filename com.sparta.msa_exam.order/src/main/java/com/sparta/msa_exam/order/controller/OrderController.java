package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.domain.AddProductToOrderRequest;
import com.sparta.msa_exam.order.domain.CreateOrderRequest;
import com.sparta.msa_exam.order.domain.ResponseDto;
import com.sparta.msa_exam.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문 추가
    @PostMapping
    public ResponseDto createOrder(@RequestBody CreateOrderRequest request) {

        return orderService.createOrder(request);
    }

    // 기존 주문에 상품 추가
    @PutMapping("/{orderId}")
    public ResponseDto addProductToOrder(
            @PathVariable Long orderId,
            @RequestBody AddProductToOrderRequest request) {
        return orderService.addProductToOrder(orderId, request);
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public ResponseDto getOneOrder(@PathVariable Long orderId) {
        return orderService.getOneOrder(orderId);
    }

}
