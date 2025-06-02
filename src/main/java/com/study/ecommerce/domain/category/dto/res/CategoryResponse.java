package com.study.ecommerce.domain.category.dto.res;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        Integer depth,
        Long parentId,
        List<CategoryResponse> children
) {
}
