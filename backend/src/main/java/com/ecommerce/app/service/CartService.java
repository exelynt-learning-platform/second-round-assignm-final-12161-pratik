package com.ecommerce.app.service;

import com.ecommerce.app.dto.cart.CartItemResponse;
import com.ecommerce.app.dto.cart.CartResponse;
import com.ecommerce.app.entity.Cart;
import com.ecommerce.app.entity.CartItem;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.exception.BadRequestException;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.repository.CartRepository;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
  private final CartRepository cartRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;

  public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
  }

  @Transactional
  public CartResponse addItem(Long userId, Long productId, int quantity) {
    Cart cart = getOrCreateCart(userId);
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

    CartItem item = cart.getCartItems().stream()
        .filter(ci -> ci.getProduct().getId().equals(productId))
        .findFirst()
        .orElse(null);

    if (item == null) {
      item = new CartItem();
      item.setCart(cart);
      item.setProduct(product);
      item.setQuantity(quantity);
      cart.getCartItems().add(item);
    } else {
      item.setQuantity(item.getQuantity() + quantity);
    }
    return toResponse(cartRepository.save(cart));
  }

  @Transactional
  public CartResponse updateItem(Long userId, Long productId, int quantity) {
    Cart cart = getOrCreateCart(userId);
    CartItem item = cart.getCartItems().stream()
        .filter(ci -> ci.getProduct().getId().equals(productId))
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
    item.setQuantity(quantity);
    return toResponse(cartRepository.save(cart));
  }

  @Transactional
  public CartResponse removeItem(Long userId, Long productId) {
    Cart cart = getOrCreateCart(userId);
    boolean removed = cart.getCartItems().removeIf(ci -> ci.getProduct().getId().equals(productId));
    if (!removed) throw new ResourceNotFoundException("Cart item not found");
    return toResponse(cartRepository.save(cart));
  }

  public CartResponse getCart(Long userId) {
    return toResponse(getOrCreateCart(userId));
  }

  @Transactional
  public void clearCart(Long userId) {
    Cart cart = getOrCreateCart(userId);
    cart.getCartItems().clear();
    cartRepository.save(cart);
  }

  private Cart getOrCreateCart(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new BadRequestException("User not found");
    }
    return cartRepository.findByUserId(userId).orElseGet(() -> {
      Cart c = new Cart();
      c.setUser(userRepository.findById(userId).orElseThrow(() -> new BadRequestException("User not found")));
      return cartRepository.save(c);
    });
  }

  private CartResponse toResponse(Cart cart) {
    CartResponse response = new CartResponse();
    response.setCartId(cart.getId());
    List<CartItemResponse> items = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    for (CartItem item : cart.getCartItems()) {
      CartItemResponse itemResponse = new CartItemResponse();
      itemResponse.setItemId(item.getId());
      itemResponse.setProductId(item.getProduct().getId());
      itemResponse.setProductName(item.getProduct().getName());
      itemResponse.setUnitPrice(item.getProduct().getPrice());
      itemResponse.setQuantity(item.getQuantity());
      BigDecimal lineTotal = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
      itemResponse.setLineTotal(lineTotal);
      total = total.add(lineTotal);
      items.add(itemResponse);
    }
    response.setItems(items);
    response.setTotal(total);
    return response;
  }
}

