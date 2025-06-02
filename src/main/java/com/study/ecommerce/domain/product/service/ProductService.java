package com.study.ecommerce.domain.product.service;

import com.study.ecommerce.domain.category.entity.Category;
import com.study.ecommerce.domain.category.repository.CategoryRepository;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import com.study.ecommerce.domain.product.dto.req.ProductCreateRequest;
import com.study.ecommerce.domain.product.dto.req.ProductUpdateRequest;
import com.study.ecommerce.domain.product.dto.res.ProductResponse;
import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.entity.Product.ProductStatus;
import com.study.ecommerce.domain.product.repository.ProductRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request, String email) {

        Member seller = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));

        Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .stockQuantity(request.stockQuantity())
                .status(ProductStatus.ACTIVE)
                .sellerId(seller.getId())
                .categoryId(category.getId())
                .build();

        productRepository.save(product);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                category.getName()
        );
    }

    @Transactional
    public void deleteProduct(Long id, String email) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        // 현 사용자가 판매자인지 확인
        Member seller = memberRepository.findById(product.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));

        if(!seller.getEmail().equals(email)) {
            throw new IllegalArgumentException("상품 삭제 권한이 없습니다.");
        }

        product.delete();
    }

    // update, id 기준으로 가져오기 숙제
    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id, String email) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        // email로 판매자 여부 확인하는 부분이 response에 없어 일단 생략
//        Member seller = memberRepository.findById(product.getSellerId())
//                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));
//        if(!seller.getEmail().equals(email)) {
//            // 판매자와 조회자가 일치하지 않을 때 :
//        }
//        // 판매자와 조회자가 일치할 때 :

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                category.getName()
        );
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request){

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        Member seller = memberRepository.findById(product.getSellerId())
                .orElseThrow(() -> new EntityNotFoundException("판매자를 찾을 수 없습니다."));
        if(!seller.getEmail().equals(request.memberEmail())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다."));

        ProductStatus status;
        try{
            status = ProductStatus.valueOf(request.status());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("유효하지 않은 상태입니다.");
        }

        // 제품 업데이트 -> jpa 더티체킹(더티캐싱) 찾아보고 만들기
        product.update(
                request.name(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                ProductStatus.valueOf(request.status()),
                category.getId()
        );

        // 반환
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                category.getName()
        );
    }

}
