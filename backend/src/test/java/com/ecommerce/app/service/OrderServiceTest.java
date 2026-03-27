package com.ecommerce.app.service;

import com.ecommerce.app.dto.cart.CartItemResponse;
import com.ecommerce.app.dto.cart.CartResponse;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock private OrderRepository orderRepository;
  @Mock private UserRepository userRepository;
  @Mock private ProductRepository productRepository;
  @Mock private CartService cartService;
  @InjectMocks private OrderService orderService;

  @Test
  void createOrderFromCart_shouldCalculateTotalAndPlaceOrder() {
    User user = new User(); user.setId(1L);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    Product p = new Product(); p.setId(2L); p.setName("P"); p.setPrice(BigDecimal.TEN); p.setStockQuantity(20);
    when(productRepository.findById(2L)).thenReturn(Optional.of(p));
    when(productRepository.save(any(Product.class))).thenReturn(p);
    when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    CartItemResponse ci = new CartItemResponse();
    ci.setProductId(2L); ci.setQuantity(3);
    CartResponse cart = new CartResponse(); cart.setItems(Arrays.asList(ci));
    when(cartService.getCart(1L)).thenReturn(cart);

    BigDecimal total = orderService.createOrderFromCart(1L, "addr").getTotalPrice();
    assertEquals(new BigDecimal("30"), total.stripTrailingZeros());
  }
}

