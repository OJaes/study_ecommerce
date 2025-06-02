package com.study.ecommerce.domain.product.entity;

import com.study.ecommerce.global.common.BaseTimeEntity;
import com.study.ecommerce.global.error.ErrorCode;
import com.study.ecommerce.global.error.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class             Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "seller_id")
    private Long sellerId;

    @Column(name = "category_id")
    private Long categoryId;

    @Builder
    public Product(String name, String description, Long price, int stockQuantity, ProductStatus status, Long sellerId, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
    }

    public enum ProductStatus {
        ACTIVE, SOLD_OUT, DELETED
    }



// businessMethod
    // decreaseStock(int quantity)
    // 에러가 터져야 하는 부분?
    // 상태 변경 -> sold_out (if)

    public void decreasesStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.stockQuantity -= restStock;
        if(this.stockQuantity == 0){
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    // increaseStock(int quantity)
    // 에러가 터져야 하는 부분

    public void increasesStock(int quantity) {
        this.stockQuantity += quantity;
        if(this.stockQuantity > 0 && this.status == ProductStatus.SOLD_OUT){
            this.status = ProductStatus.ACTIVE;
        }
    }

    // update -> 전체 seller id는 바뀔 수 없다

    public void update(String name, String description, Long price, int stockQuantity, ProductStatus status, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.categoryId = categoryId;
    }

    // delete -> soft delete (상태 변경)
    public void delete() {
        status = ProductStatus.DELETED;
    }
}
