package com.study.ecommerce.domain.cart.service.command;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;

public interface CartCommandService {

    Cart createCart(Long memberId);

    CartItem addCartItem(Long cartId, Long productId, Integer quantity);

    CartItem updateCartItemQuantity(Long cartItem, Integer quantity);

    void removeCartItem(Long cartItemId);

    void clearCart(Long cartId);
}
