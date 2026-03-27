package com.ecommerce.app.service;

import com.ecommerce.app.dto.product.ProductRequest;
import com.ecommerce.app.dto.product.ProductResponse;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.exception.ResourceNotFoundException;
import com.ecommerce.app.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductResponse create(ProductRequest request) {
    Product product = toEntity(request);
    return toResponse(productRepository.save(product));
  }

  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
  }

  public ProductResponse getById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    return toResponse(product);
  }

  public ProductResponse update(Long id, ProductRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setPrice(request.getPrice());
    product.setStockQuantity(request.getStockQuantity());
    product.setImageUrl(request.getImageUrl());
    return toResponse(productRepository.save(product));
  }

  public void delete(Long id) {
    if (!productRepository.existsById(id)) {
      throw new ResourceNotFoundException("Product not found");
    }
    productRepository.deleteById(id);
  }

  private Product toEntity(ProductRequest request) {
    Product p = new Product();
    p.setName(request.getName());
    p.setDescription(request.getDescription());
    p.setPrice(request.getPrice());
    p.setStockQuantity(request.getStockQuantity());
    p.setImageUrl(request.getImageUrl());
    return p;
  }

  private ProductResponse toResponse(Product p) {
    ProductResponse response = new ProductResponse();
    response.setId(p.getId());
    response.setName(p.getName());
    response.setDescription(p.getDescription());
    response.setPrice(p.getPrice());
    response.setStockQuantity(p.getStockQuantity());
    response.setImageUrl(p.getImageUrl());
    return response;
  }
}

