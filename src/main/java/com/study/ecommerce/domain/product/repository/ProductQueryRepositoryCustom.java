package com.study.ecommerce.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.ecommerce.domain.category.entity.QCategory;
import com.study.ecommerce.domain.product.dto.req.ProductSearchCondition;
import com.study.ecommerce.domain.product.dto.resp.ProductSummaryDto;
import com.study.ecommerce.domain.product.entity.Product.ProductStatus;
import com.study.ecommerce.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProductQueryRepositoryCustom implements ProductQueryRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductSummaryDto> searchProducts(
            ProductSearchCondition condition,
            Pageable pageable
    ) {

        // sql Select product as product랑 같다보 보면 될 듯
        // alias 주고 가져오는 부분?
        QProduct product = QProduct.product;
        QCategory category = QCategory.category;

        // sql where 부분?
        // 동적 쿼리를 생성하기 위한 조건
        BooleanBuilder builder = new BooleanBuilder();

        // 검색 단어가 상품명이나 설명에 포함되었는가?
        if (StringUtils.hasText(condition.keyword())) {
            builder.and(product.name.containsIgnoreCase(condition.keyword())
                    .or(product.description.containsIgnoreCase(condition.keyword())));
        }

        // 가격 특정 카테고리에 대한 검색인가?
        // eq = equal
        if (condition.categoryId() != null) {
            builder.and(product.categoryId.eq(condition.categoryId()));
        }

        // 최소가격이 정해졌는가?
        // goe = greater or equal >=
        if (condition.minPrice() != null) {
            builder.and(product.price.goe(condition.minPrice()));
        }

        // 최대 가격이 정해졌는가?
        // loe = less or equal <=
        if (condition.maxPrice() != null) {
            builder.and(product.price.loe(condition.maxPrice()));
        }

        // 판매자 아이디 필터링
        if (condition.sellerId() != null) {
            builder.and(product.sellerId.eq(condition.sellerId()));
        }

        // 상태는 ACTIVE인 것만 검색
        builder.and(product.status.eq(ProductStatus.ACTIVE));

        // builder로 만든 조건에 맞는 상품의 총 개수를 세는 쿼리
        // 전체 카운트 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .where(builder);

        // 조인 없이 카테고리 이름을 가져오기 위한 서브 쿼리방식
        // 실제 조회 쿼리
        List<ProductSummaryDto> summaryDtos = queryFactory
                .select(Projections.constructor(ProductSummaryDto.class,
                        product.id,
                        product.name,
                        product.price,
                        product.stockQuantity,
                        // 카테고리 이름 대신 상수값을 변환
//                        Expressions.asString("Category").as("categoryName"), 기존
                        JPAExpressions
                                .select(category.name)
                                .from(category)
                                .where(category.id.eq(product.categoryId)),
                        product.status))
                .from(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, product))
                .fetch();

        return PageableExecutionUtils.getPage(summaryDtos, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable, QProduct product) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "price" :
                        return order.isAscending() ? product.price.asc() : product.price.desc();

                    case "createdAt":
                        return order.isAscending() ? product.createdAt.asc() : product.createdAt.desc();

                    default:
                        return product.id.desc();
                }
            }
        }

        return product.id.desc();
    }
}