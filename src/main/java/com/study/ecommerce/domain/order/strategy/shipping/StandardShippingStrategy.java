package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class StandardShippingStrategy implements ShippingStrategy {

    // 기본 배송비
    // 무료 배송 기준

//    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5000);

//    private static final int BASE_DELIVERYDAY = 3;

    private static final BigDecimal SHIPPING_COST = new BigDecimal("3000");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("50000");
    private static final int DELIVERY_DAYS = 3;

    @Override
    public BigDecimal calculateShippingCost(Order order) {
//        return BASE_COST;
        if (order.getTotalAmount().compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }

        return SHIPPING_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "일반배송 (3일)";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
//        return BASE_DELIVERYDAY + (int) Math.random() * 3;
        return DELIVERY_DAYS;
    }
}
