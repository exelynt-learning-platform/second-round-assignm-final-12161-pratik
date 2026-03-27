package com.ecommerce.app.config;

import com.ecommerce.app.entity.Cart;
import com.ecommerce.app.entity.Product;
import com.ecommerce.app.entity.Role;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.repository.CartRepository;
import com.ecommerce.app.repository.ProductRepository;
import com.ecommerce.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataInitializer {
  @Bean
  CommandLineRunner initData(UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository,
                             PasswordEncoder encoder) {
    return args -> {
      User admin;
      if (!userRepository.existsByEmail("admin@shop.com")) {
        admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@shop.com");
        admin.setPassword(encoder.encode("Admin@123"));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);
      } else {
        admin = userRepository.findByEmail("admin@shop.com").orElse(null);
      }

      if (admin != null && !cartRepository.findByUserId(admin.getId()).isPresent()) {
        Cart cart = new Cart();
        cart.setUser(admin);
        cartRepository.save(cart);
      }

      if (productRepository.count() == 0) {
        Product p1 = new Product();
        p1.setName("Wireless Headphones");
        p1.setDescription("Comfort fit, noise isolation, 30-hour battery life.");
        p1.setPrice(new BigDecimal("59.99"));
        p1.setStockQuantity(25);
        p1.setImageUrl("https://via.placeholder.com/400x300?text=Headphones");

        Product p2 = new Product();
        p2.setName("Smart Watch");
        p2.setDescription("Health tracking, notifications, and water resistance.");
        p2.setPrice(new BigDecimal("89.99"));
        p2.setStockQuantity(15);
        p2.setImageUrl("https://via.placeholder.com/400x300?text=Smart+Watch");

        Product p3 = new Product();
        p3.setName("Laptop Backpack");
        p3.setDescription("Durable, lightweight, fits up to 15.6-inch laptops.");
        p3.setPrice(new BigDecimal("39.99"));
        p3.setStockQuantity(40);
        p3.setImageUrl("https://via.placeholder.com/400x300?text=Backpack");

        productRepository.saveAll(Arrays.asList(p1, p2, p3));
      }
    };
  }
}

