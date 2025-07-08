package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class ExpressShippingStrategy implements ShippingStrategy {
//    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5000);
//    private static final BigDecimal SAME_DAY_EXTRA_COST = BigDecimal.valueOf(2000);

    // 배송비
    private static final BigDecimal SHIPPING_COST = new BigDecimal("6000");
    // 당일인지 익일인지  = 1
    private static final int DELIVERY_DAYS = 1;


    @Override
    public BigDecimal calculateShippingCost(Order order) {
//        BigDecimal cost = BASE_COST;
//        if (order.isSameDayDeliver()) {
//            cost = cost.add(SAME_DAY_EXTRA_COST);
//        }
//        return cost;
        return SHIPPING_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "익스프레스 배송 (당일/익일)";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {

//        return order.isSameDayDeliver() ? 1 : 2;
        return DELIVERY_DAYS;
    }



}

