package com.study.ecommerce.domain.cart.service.command;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;
import com.study.ecommerce.domain.cart.repository.CartItemRepository;
import com.study.ecommerce.domain.cart.repository.CartRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartCommandServiceImpl implements CartCommandService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart createCart(Long memberId) {
        Cart cart = new Cart(memberId);
        return cartRepository.save(cart);
    }

    @Override
    public CartItem addCartItem(Long cartId, Long productId, Integer quantity) {
        CartItem cartItem = CartItem.builder()
                .cartId(cartId)
                .productId(productId)
                .quantity(quantity)
                .build();

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItem, Integer quantity) {
        // cartItemId -> cartItem
        CartItem item = cartItemRepository.findById(cartItem)
                .orElseThrow(() -> new EntityNotFoundException("장바구니의 상품을 찾을 수 없습니다."));
        // cartItem.update
        item.updateQuantity(quantity);
        // save
        return cartItemRepository.save(item);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        // cartItem
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("장바구니의 상품을 찾을 수 없습니다."));
        // repository.delete
        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart(Long cartId) {
        // findByCartId list
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        // list 전체 삭제
        cartItemRepository.deleteAll(cartItems);
    }
}
