package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest.OrderItemRequest;
import com.study.ecommerce.domain.order.service.OrderService;
import com.study.ecommerce.domain.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryValidationHandler extends OrderValidationHandler{

    private ProductService productService;

    @Override
    protected void doValidate(OrderCreateRequest request) {
        // for문 ->
        for (OrderItemRequest item : request.items()) {
            validateInventory(item);
        }
    }

    private void validateInventory(OrderItemRequest item) {
        int requestQuantity = item.getQuantity();
        Long productId = item.getProductId();

        // 현재 재고 조회 ->  변수할당
        int productQuantity = productService.getProduct(productId).stockQuantity();
        // 재고 부족 검증
        if(productQuantity <= 0 || productQuantity > requestQuantity) {
            fail("재고가 부족합니다.");
        }
        // 예약된 재고 고려

    }

    @Override
    protected String getHandlerName() {
        return "";
    }

    private int getAvailableStock(Long productId) {
        return 90;
    }
}