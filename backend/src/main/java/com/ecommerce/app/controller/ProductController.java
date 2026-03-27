package com.ecommerce.app.controller;

import com.ecommerce.app.dto.product.ProductRequest;
import com.ecommerce.app.dto.product.ProductResponse;
import com.ecommerce.app.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getAll() {
    return ResponseEntity.ok(productService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(productService.create(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
    return ResponseEntity.ok(productService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

