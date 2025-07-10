package com.study.ecommerce.domain.cart.facade;

import com.study.ecommerce.domain.cart.dto.CartItemRequest;
import com.study.ecommerce.domain.cart.dto.CartItemResponse;
import com.study.ecommerce.domain.cart.dto.CartResponse;
import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;
import com.study.ecommerce.domain.cart.service.command.CartCommandService;
import com.study.ecommerce.domain.cart.service.query.CartQueryService;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.service.query.MemberQueryService;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartFacadeServiceImpl implements CartFacadeService{
    private final CartCommandService cartCommandService;
    private final CartQueryService cartQueryService;
    private final MemberQueryService memberQueryService;
    // private final ProductQueryService productQueryService;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String email) {
        // 회원 조회
        Member member = memberQueryService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 장바구니 조회
        Cart cart = cartQueryService.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("장바구니를 찾을 수 없습니다."));

        // 장바구니 아이템들을 조회
        List<CartItem> cartItems = cartQueryService.findByCartId(cart.getId());

        List<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(cartItem -> {
                    // productQueryService

                    return new CartItemResponse(
                            cartItem.getId(),
                            cartItem.getProductId(),
                            "",
                            0L,
                            cartItem.getQuantity(),
                            0L

                    );
                })
                .toList();

        long totalAmount = cartItemResponses.stream()
                .mapToLong(CartItemResponse::totalPrice)
                .sum();

        return new CartResponse(cart.getId(), totalAmount, cartItemResponses);
    }

    @Override
    public CartItemResponse addItemToCart(String email, CartItemRequest request) {
        // 회원 조회
        Member member = memberQueryService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 장바구니 조회 (없으면 생성)
        Cart cart = cartQueryService.findByMemberId(member.getId())
                .orElseGet(() -> cartCommandService.createCart(member.getId()));

        // 상품 존재 확인

        // 장바구니에 상품을 추가
        return null;
    }

    @Override
    public CartItemResponse updateCartItemQuantity(String email, Long cartItemId, Integer quantity) {
        validateCartItemOwnership(email, cartItemId);

        CartItem cartItem = cartCommandService.updateCartItemQuantity(cartItemId, quantity);
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProductId(),
                "",
                0L,
                cartItem.getQuantity(),
                0L
        );
    }

    @Override
    public void removeCartItem(String email, Long cartItemId) {
        validateCartItemOwnership(email, cartItemId);
        cartCommandService.removeCartItem(cartItemId);
    }

    @Override
    public void clearCart(String email) {
        Member member = memberQueryService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Cart cart = cartQueryService.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("장바구니를 찾을 수 없습니다."));

        cartCommandService.clearCart(cart.getId());
    }

    private void validateCartItemOwnership(String email, Long cartItemId) {
        Member member = memberQueryService.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        CartItem cartItem = cartQueryService.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("장바구니 아이템을 찾을 수 없습니다."));

        Cart cart = cartQueryService.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("장바구니를 찾을 수 없습니다."));

        if (!cartItem.getCartId().equals(cart.getId())) {
            throw new IllegalArgumentException("장바구니 아이템 접근 권한이 없습니다.");
        }

    }
}