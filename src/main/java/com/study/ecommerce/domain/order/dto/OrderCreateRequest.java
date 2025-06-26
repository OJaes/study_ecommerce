package com.study.ecommerce.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public record OrderCreateRequest(
        List<Long> cartItemIds,

        @Valid
        List<OrderItemRequest> items,

        @NotNull
        Boolean payNow,

        String paymentMethod
) {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @NotNull
        @Positive
        private Integer quantity;
    }
}
