package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class StandardShippingStrategy implements ShippingStrategy {

    // 기본 배송비
    // 무료 배송 기준

    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5000);
    private static final int BASE_DELIVERYDAY = 3;

    @Override
    public BigDecimal calculateShippingCost(Order order) {
        return BASE_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "Standard";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return BASE_DELIVERYDAY + (int) Math.random() * 3;
    }
}
