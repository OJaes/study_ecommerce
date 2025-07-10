package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest.OrderItemRequest;
import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductValidationHandler extends OrderValidationHandler{
    // static 변수
    private final ProductRepository productRepository;


    @Override
    protected void doValidate(OrderCreateRequest request) {
        for (OrderItemRequest orderItem : request.items()) {
            validateOrderItem(orderItem);
        }
    }

    private void validateOrderItem(OrderItemRequest orderItem) {
        // 상품 id 검증(db) ->
        // 상품 존재 여부 검증 (실제로는 DB 참조)
        if(!validateProductId(orderItem)){
            fail("존재하지 않는 상품입니다.");
        }
        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        // 수량 검증(db) ->
        // 최대 주문 수량 검증
        if(!validateProductQuantity(orderItem, product)){
            fail("수량이 충분하지 않습니다.");
        };

        // 상품 가격 검증



        // 상품 판매 가능 여부 검증 (db)
    }

    private boolean validateProductId(OrderItemRequest orderItem) {
        return productRepository.existsById(orderItem.getProductId());
    }

    private boolean validateProductQuantity(OrderItemRequest orderItem, Product product) {
        return (product.getStockQuantity() >= orderItem.getQuantity() && product.getStockQuantity() >= 0);
    }

    private boolean validateProductPrice(OrderItemRequest orderItem){


    }

    @Override
    protected String getHandlerName() {

        return "상품검증핸들러";
    }




}