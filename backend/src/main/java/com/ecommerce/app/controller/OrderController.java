package com.ecommerce.app.controller;

import com.ecommerce.app.dto.order.CreateOrderRequest;
import com.ecommerce.app.dto.order.OrderResponse;
import com.ecommerce.app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;
  private final AuthenticatedUserResolver userResolver;

  public OrderController(OrderService orderService, AuthenticatedUserResolver userResolver) {
    this.orderService = orderService;
    this.userResolver = userResolver;
  }

  @PostMapping
  public ResponseEntity<OrderResponse> create(Authentication authentication, @Valid @RequestBody CreateOrderRequest request) {
    return ResponseEntity.ok(orderService.createOrderFromCart(userResolver.getUserId(authentication), request.getShippingAddress()));
  }

  @GetMapping
  public ResponseEntity<List<OrderResponse>> list(Authentication authentication) {
    return ResponseEntity.ok(orderService.getUserOrders(userResolver.getUserId(authentication)));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponse> details(Authentication authentication, @PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.getOrderDetails(userResolver.getUserId(authentication), orderId));
  }

  @PostMapping("/{orderId}/cancel")
  public ResponseEntity<OrderResponse> cancel(Authentication authentication, @PathVariable Long orderId) {
    return ResponseEntity.ok(orderService.cancelOrder(userResolver.getUserId(authentication), orderId));
  }
}

