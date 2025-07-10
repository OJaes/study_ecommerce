package com.study.ecommerce.domain.cart.service.query;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;
import com.study.ecommerce.domain.cart.repository.CartItemRepository;
import com.study.ecommerce.domain.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartQueryServiceImpl implements CartQueryService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Optional<Cart> findByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }

    @Override
    public Optional<CartItem> findById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    @Override
    public boolean existsByMemberId(Long memberId) {
        return cartRepository.existsByMemberId(memberId);
    }
}