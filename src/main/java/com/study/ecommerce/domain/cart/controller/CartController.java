package com.study.ecommerce.domain.cart.controller;

import com.study.ecommerce.domain.cart.dto.CartItemRequest;
import com.study.ecommerce.domain.cart.dto.CartItemResponse;
import com.study.ecommerce.domain.cart.dto.CartResponse;
import com.study.ecommerce.domain.cart.facade.CartFacadeService;
import com.study.ecommerce.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartFacadeService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        CartResponse response = cartService.getCart(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addCartItem(
            @Valid @RequestBody CartItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CartItemResponse response = cartService.addItemToCart(userDetails.getUsername(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        CartItemResponse response = cartService.updateCartItemQuantity(userDetails.getUsername(), cartItemId, request.quantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(
            @PathVariable Long cartItemId,
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.removeCartItem(userDetails.getUsername(), cartItemId);
        return ResponseEntity.noContent().build();
    }
}