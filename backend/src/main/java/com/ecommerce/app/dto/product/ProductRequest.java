package com.ecommerce.app.dto.product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequest {
  @NotBlank
  private String name;
  @NotBlank
  private String description;
  @NotNull
  @DecimalMin(value = "0.01", message = "Price must be greater than 0")
  private BigDecimal price;
  @Min(0)
  private int stockQuantity;
  private String imageUrl;
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public BigDecimal getPrice() { return price; }
  public void setPrice(BigDecimal price) { this.price = price; }
  public int getStockQuantity() { return stockQuantity; }
  public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
  public String getImageUrl() { return imageUrl; }
  public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}

