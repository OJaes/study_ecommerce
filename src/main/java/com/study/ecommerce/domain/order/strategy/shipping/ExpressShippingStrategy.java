package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class ExpressShippingStrategy implements ShippingStrategy {
    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5000);
    private static final BigDecimal SAME_DAY_EXTRA_COST = BigDecimal.valueOf(2000);

    @Override
    public BigDecimal calculateShippingCost(Order order) {
        BigDecimal cost = BASE_COST;
        if (order.isSameDayDeliver()) {
            cost = cost.add(SAME_DAY_EXTRA_COST);
        }
        return cost;
    }

    @Override
    public String getShippingPolicyName() {
        return "Express";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return order.isSameDayDeliver() ? 1 : 2;
    }


}

