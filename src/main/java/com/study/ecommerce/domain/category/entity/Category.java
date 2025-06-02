package com.study.ecommerce.domain.category.entity;

import com.study.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer depth;

    @Column(name = "parent_id")
    private Long parentId;

    @Builder
    public Category(String name, Integer depth, Long parentId) {
        this.name = name;
        this.depth = depth;
        this.parentId = parentId;
    }
}
