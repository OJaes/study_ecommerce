package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class EconomyShippingStrategy implements ShippingStrategy {

    // 배송비
    // 무료배송 기준

    private static final int BASE_DELIVERYDAY = 3;

    //배송 날짜

    @Override
    public BigDecimal calculateShippingCost(Order order) {
        return BigDecimal.ZERO;
    }

    @Override
    public String getShippingPolicyName() {
        return "Economy";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return BASE_DELIVERYDAY + (int) Math.random() * 5;
    }
}
