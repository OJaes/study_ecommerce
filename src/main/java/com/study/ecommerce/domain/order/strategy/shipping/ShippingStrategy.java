package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public interface ShippingStrategy {

    // 배송비 계산
    BigDecimal calculateShippingCost(Order order);

    // 배송 정책명 반환
    String getShippingPolicyName();

    // 예상 배송일 반환
    int getEstimatedDeliveryDays(Order order);
}
