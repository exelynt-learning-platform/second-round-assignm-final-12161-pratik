package com.ecommerce.app.dto.order;

import com.ecommerce.app.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {
  private Long id;
  private BigDecimal totalPrice;
  private String shippingAddress;
  private OrderStatus status;
  private List<OrderItemResponse> orderItems;
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public BigDecimal getTotalPrice() { return totalPrice; }
  public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
  public String getShippingAddress() { return shippingAddress; }
  public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
  public OrderStatus getStatus() { return status; }
  public void setStatus(OrderStatus status) { this.status = status; }
  public List<OrderItemResponse> getOrderItems() { return orderItems; }
  public void setOrderItems(List<OrderItemResponse> orderItems) { this.orderItems = orderItems; }
}

