package com.ecommerce.app.repository;

import com.ecommerce.app.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserIdOrderByIdDesc(Long userId);

  @Override
  @EntityGraph(attributePaths = {"orderItems", "orderItems.product", "user"})
  Optional<Order> findById(Long id);
}

