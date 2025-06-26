package com.study.ecommerce.domain.order.entity;

import com.study.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    private Long totalAmount;

    @Builder
    public Order(Long memberId, OrderStatus status, LocalDateTime orderDate, Long totalAmount) {
        this.memberId = memberId;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    // 비지니스 메소드
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public enum OrderStatus {
        CREATED, PAID, CANCELED, SHIPPING, DELIVERED
    }
}
