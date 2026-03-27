package com.ecommerce.app.controller;

import com.ecommerce.app.dto.cart.AddCartItemRequest;
import com.ecommerce.app.dto.cart.CartResponse;
import com.ecommerce.app.dto.cart.UpdateCartItemRequest;
import com.ecommerce.app.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
  private final CartService cartService;
  private final AuthenticatedUserResolver userResolver;

  public CartController(CartService cartService, AuthenticatedUserResolver userResolver) {
    this.cartService = cartService;
    this.userResolver = userResolver;
  }

  @GetMapping
  public ResponseEntity<CartResponse> viewCart(Authentication authentication) {
    return ResponseEntity.ok(cartService.getCart(userResolver.getUserId(authentication)));
  }

  @PostMapping("/items")
  public ResponseEntity<CartResponse> addItem(Authentication authentication, @Valid @RequestBody AddCartItemRequest request) {
    return ResponseEntity.ok(
        cartService.addItem(userResolver.getUserId(authentication), request.getProductId(), request.getQuantity()));
  }

  @PutMapping("/items/{productId}")
  public ResponseEntity<CartResponse> updateItem(Authentication authentication, @PathVariable Long productId,
                                                 @Valid @RequestBody UpdateCartItemRequest request) {
    return ResponseEntity.ok(cartService.updateItem(userResolver.getUserId(authentication), productId, request.getQuantity()));
  }

  @DeleteMapping("/items/{productId}")
  public ResponseEntity<CartResponse> removeItem(Authentication authentication, @PathVariable Long productId) {
    return ResponseEntity.ok(cartService.removeItem(userResolver.getUserId(authentication), productId));
  }
}

