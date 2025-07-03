package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderCanceledEvent extends OrderEvent {

    private final String cancelReason;


    public OrderCanceledEvent(Object source, Order order, String cancelReason) {
        super(source, order, "ORDER_CANCELED");
        this.cancelReason = cancelReason;
    }
}
