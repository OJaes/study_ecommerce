package com.study.ecommerce.domain.category.service;

import com.study.ecommerce.domain.category.dto.req.CategoryRequest;
import com.study.ecommerce.domain.category.dto.res.CategoryResponse;
import com.study.ecommerce.domain.category.entity.Category;
import com.study.ecommerce.domain.category.repository.CategoryRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceCustom implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        // 모든 카테고리 조회
        List<Category> allCategories = categoryRepository.findAll();

        // 부모 Id별로 하위 카테고리 그룹화
        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat->cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        // 루트 카테고리만 필터링
        List<Category> rootCategories = allCategories.stream()
                .filter(cat -> cat.getParentId() == null)
                .toList();

        return rootCategories.stream()
                .map(root->buildCategoryHierarchy(root, childrenMap))
                .toList();
    }

    private CategoryResponse  buildCategoryHierarchy(Category category, Map<Long, List<Category>> childrenMap) {
        List<CategoryResponse> children = new ArrayList<>();
        if(childrenMap.containsKey(category.getId())) {         // 재귀
            children = childrenMap.get(category.getId()).stream()
                    .map(child->buildCategoryHierarchy(child,childrenMap))
                    .toList();
        }

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParentId(),
                children
        );
    };

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Long parentId = null;
        int depth = 1;

        if(request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId()).orElseThrow(()-> new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다."));
            parentId = parent.getId();
            depth = parent.getDepth() + 1;
        }

        Category category = Category.builder()
                .name(request.name())
                .depth(depth)
                .parentId(parentId)
                .build();
        categoryRepository.save(category);

        return new CategoryResponse(category.getId(),
                category.getName(),
                category.getDepth(),
                category.getParentId(),
                List.of()
        );
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat->cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        Category rootCategory = allCategories.stream()
                .filter(cat->cat.getId().equals(id))
                .findFirst()
                .orElseThrow(()-> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        return buildCategoryHierarchy(rootCategory, childrenMap);
    }


    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {

        List<Category> allCategories = categoryRepository.findAll();

        Category category = allCategories.stream()
                .filter(cat -> cat.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

        if (id.equals(categoryRequest.parentId())) {
            throw new IllegalArgumentException("카테고리는 자기 자신을 부모로 설정할 수 없습니다.");
        }

        category.setName(categoryRequest.name());
        category.setParentId(categoryRequest.parentId());

        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat -> cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        return buildCategoryHierarchy(category, childrenMap);
    }


    @Override
    public void deleteCategory(Long id) {
        List<Category> allCategories = categoryRepository.findAll();

        Map<Long, List<Category>> childrenMap = allCategories.stream()
                .filter(cat -> cat.getParentId() != null)
                .collect(Collectors.groupingBy(Category::getParentId));

        Category root = allCategories.stream()
                .filter(cat -> cat.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다."));

        List<Long> toDeleteIds = new ArrayList<>();
        collectAllDescendantIds(root.getId(), childrenMap, toDeleteIds);
        toDeleteIds.add(root.getId());

        categoryRepository.deleteAllById(toDeleteIds);
    }

    private void collectAllDescendantIds(Long parentId, Map<Long, List<Category>> childrenMap, List<Long> result) {
        if (!childrenMap.containsKey(parentId)) return;
        for (Category child : childrenMap.get(parentId)) {
            result.add(child.getId());
            collectAllDescendantIds(child.getId(), childrenMap, result);
        }
    }
}
