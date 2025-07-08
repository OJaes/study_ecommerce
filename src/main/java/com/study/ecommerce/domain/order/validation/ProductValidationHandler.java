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
        validateProductId(orderItem);
        // 수량 검증(db) ->
        validateProductQuantity(orderItem);
        // 최대 주문 수량 검증

        // 상품 가격 검증

        // 상품 존재 여부 검증 (실제로는 DB 참조)

        // 상품 판매 가능 여부 검증 (db)
    }

    private boolean validateProductId(OrderItemRequest orderItem) {
        return productRepository.existsById(orderItem.getProductId());
    }

    private boolean validateProductQuantity(OrderItemRequest orderItem) {
        Product product = productRepository.findById(orderItem.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return (product.getStockQuantity() >= orderItem.getQuantity() && product.getStockQuantity() >= 0);
    }


    @Override
    protected String getHandlerName() {
        return "상품검증핸들러";
    }




}