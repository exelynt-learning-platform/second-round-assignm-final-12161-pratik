package com.ecommerce.app.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cart_items", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cart_item_cart_product", columnNames = {"cart_id", "product_id"})
})
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "cart_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cart_item_cart"))
  private Cart cart;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cart_item_product"))
  private Product product;

  @Min(1)
  @Column(nullable = false)
  private int quantity;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}

