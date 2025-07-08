package com.study.ecommerce.domain.cart.service.query;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;

import java.util.List;
import java.util.Optional;

public class CartQueryServiceImpl implements CartQueryService{
    @Override
    public Optional<Cart> findByMemberId(Long memberId) {
        return Optional.empty();
    }

    @Override
    public Optional<Cart> findById(Long cartItemId) {
        return Optional.empty();
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return List.of();
    }

    @Override
    public boolean existsByMemberId(Long memberId) {
        return false;
    }
}
