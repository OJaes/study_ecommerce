package com.study.ecommerce.domain.cart.service.query;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartQueryService {

    Optional<Cart> findByMemberId(Long memberId);

    Optional<CartItem> findById(Long cartItemId);

    List<CartItem> findByCartId(Long cartId);

    boolean existsByMemberId(Long memberId);
}