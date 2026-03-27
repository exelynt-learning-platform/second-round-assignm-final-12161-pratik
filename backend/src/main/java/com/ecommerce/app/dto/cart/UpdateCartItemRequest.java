package com.ecommerce.app.dto.cart;

import javax.validation.constraints.Min;

public class UpdateCartItemRequest {
  @Min(1)
  private int quantity;
  public int getQuantity() { return quantity; }
  public void setQuantity(int quantity) { this.quantity = quantity; }
}

