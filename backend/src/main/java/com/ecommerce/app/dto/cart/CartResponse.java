package com.ecommerce.app.dto.cart;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
  private Long cartId;
  private List<CartItemResponse> items;
  private BigDecimal total;
  public Long getCartId() { return cartId; }
  public void setCartId(Long cartId) { this.cartId = cartId; }
  public List<CartItemResponse> getItems() { return items; }
  public void setItems(List<CartItemResponse> items) { this.items = items; }
  public BigDecimal getTotal() { return total; }
  public void setTotal(BigDecimal total) { this.total = total; }
}

