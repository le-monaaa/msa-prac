package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.domain.*;
import com.sparta.msa_exam.order.feign.ProductClient;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    // 주문 추가
    public ResponseDto createOrder(CreateOrderRequest request) {
        try {
            List<Long> productIds = request.getProductIds();
            ResponseDto response = productClient.validateProducts(productIds);
            if(response.getCode() != ResponseDto.SUCCESS) {
                return new ResponseDto<>(ResponseDto.FAILURE, "유효하지 않은 상품 번호가 포함되어 있습니다", null);
            }

            Order order = orderRepository.save(Order.builder()
                    .productIds(productIds)
                    .build());

            OrderResponse orderResponse = OrderResponse.builder()
                    .orderId(order.getId())
                    .productIds(order.getProductIds())
                    .build();

            return new ResponseDto(ResponseDto.SUCCESS, "주문 생성 성공", orderResponse);

        } catch (Exception e) {
            log.error("Error occurred while creating order: {}", e.getMessage(), e);
            return new ResponseDto(ResponseDto.FAILURE, "잠시 후에 주문 추가를 요청 해주세요", null);
        }
    }

    // 기존 주문에 상품 추가
    public ResponseDto addProductToOrder(Long orderId, AddProductToOrderRequest request) {

        // 주문 체크
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            return new ResponseDto(ResponseDto.FAILURE, "해당하는 주문이 없습니다", null);
        }

        // 상품 유효성 체크
        ResponseDto response = productClient.validateProduct(request.getProductId());
        if(response.getCode() != ResponseDto.SUCCESS) {
            return response;
        }
        // productIds가 null이거나 Immutable인 경우
        List<Long> productIds = order.getProductIds() != null ? new ArrayList<>(order.getProductIds()) : new ArrayList<>();
        productIds.add(request.getProductId());
        order.setProductIds(productIds);

        orderRepository.save(order);

        return new ResponseDto(ResponseDto.SUCCESS, "상품 추가 완료", null);

    }

    // 주문 단건 조회
    public ResponseDto getOneOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order == null) {
            return new ResponseDto(ResponseDto.FAILURE, "해당하는 주문이 없습니다", null);
        }
        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .productIds(order.getProductIds())
                .build();
        return new ResponseDto(ResponseDto.SUCCESS, "주문 조회 성공", orderResponse);
    }




}
