package com.ecommerce.app.service;

import com.ecommerce.app.dto.cart.CartResponse;
import com.ecommerce.app.dto.order.OrderItemResponse;
import com.ecommerce.app.dto.order.OrderResponse;
import com.ecommerce.app.entity.*;
import com.ecommerce.app.exception.BadRequestException;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.repository.OrderRepository;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CartService cartService;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository,
                      CartService cartService) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.cartService = cartService;
  }

  @Transactional
  public OrderResponse createOrderFromCart(Long userId, String shippingAddress) {
    User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    CartResponse cart = cartService.getCart(userId);
    if (cart.getItems().isEmpty()) {
      throw new BadRequestException("Cart is empty");
    }

    Order order = new Order();
    order.setUser(user);
    order.setShippingAddress(shippingAddress);
    order.setStatus(OrderStatus.PLACED);

    BigDecimal total = BigDecimal.ZERO;
    List<OrderItem> items = new ArrayList<>();
    for (com.ecommerce.app.dto.cart.CartItemResponse ci : cart.getItems()) {
      Product product = productRepository.findById(ci.getProductId())
          .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
      if (product.getStockQuantity() < ci.getQuantity()) {
        throw new BadRequestException("Insufficient stock for product: " + product.getName());
      }
      product.setStockQuantity(product.getStockQuantity() - ci.getQuantity());
      productRepository.save(product);

      OrderItem item = new OrderItem();
      item.setOrder(order);
      item.setProduct(product);
      item.setQuantity(ci.getQuantity());
      item.setPrice(product.getPrice());
      total = total.add(product.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
      items.add(item);
    }
    order.setOrderItems(items);
    order.setTotalPrice(total);
    Order saved = orderRepository.save(order);

    cartService.clearCart(userId);
    return toResponse(saved);
  }

  public List<OrderResponse> getUserOrders(Long userId) {
    return orderRepository.findByUserIdOrderByIdDesc(userId).stream().map(this::toResponse).collect(Collectors.toList());
  }

  public OrderResponse getOrderDetails(Long userId, Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (!order.getUser().getId().equals(userId)) {
      throw new BadRequestException("You can only access your own order");
    }
    return toResponse(order);
  }

  @Transactional
  public OrderResponse cancelOrder(Long userId, Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    if (!order.getUser().getId().equals(userId)) {
      throw new BadRequestException("You can only cancel your own order");
    }
    if (order.getStatus() == OrderStatus.CANCELLED) {
      return toResponse(order);
    }
    order.setStatus(OrderStatus.CANCELLED);
    for (OrderItem item : order.getOrderItems()) {
      Product p = item.getProduct();
      p.setStockQuantity(p.getStockQuantity() + item.getQuantity());
      productRepository.save(p);
    }
    return toResponse(orderRepository.save(order));
  }

  private OrderResponse toResponse(Order order) {
    OrderResponse response = new OrderResponse();
    response.setId(order.getId());
    response.setTotalPrice(order.getTotalPrice());
    response.setShippingAddress(order.getShippingAddress());
    response.setStatus(order.getStatus());
    List<OrderItemResponse> items = order.getOrderItems().stream().map(i -> {
      OrderItemResponse r = new OrderItemResponse();
      r.setProductId(i.getProduct().getId());
      r.setProductName(i.getProduct().getName());
      r.setQuantity(i.getQuantity());
      r.setPrice(i.getPrice());
      r.setLineTotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
      return r;
    }).collect(Collectors.toList());
    response.setOrderItems(items);
    return response;
  }
}

