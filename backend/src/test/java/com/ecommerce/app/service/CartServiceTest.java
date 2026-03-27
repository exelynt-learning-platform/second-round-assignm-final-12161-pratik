package com.ecommerce.app.service;

import com.ecommerce.app.entity.Cart;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.repository.CartRepository;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
  @Mock private CartRepository cartRepository;
  @Mock private ProductRepository productRepository;
  @Mock private UserRepository userRepository;
  @InjectMocks private CartService cartService;

  @Test
  void addItem_shouldAddProductToCart() {
    when(userRepository.existsById(1L)).thenReturn(true);
    User user = new User(); user.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    Cart cart = new Cart(); cart.setId(1L); cart.setUser(user);
    when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
    when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));
    Product product = new Product(); product.setId(2L); product.setName("P"); product.setPrice(BigDecimal.TEN);
    when(productRepository.findById(2L)).thenReturn(Optional.of(product));

    int qty = cartService.addItem(1L, 2L, 3).getItems().get(0).getQuantity();
    assertEquals(3, qty);
  }
}

