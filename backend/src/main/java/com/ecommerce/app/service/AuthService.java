package com.ecommerce.app.service;

import com.ecommerce.app.dto.auth.AuthResponse;
import com.ecommerce.app.dto.auth.LoginRequest;
import com.ecommerce.app.dto.auth.RegisterRequest;
import com.ecommerce.app.entity.Cart;
import com.ecommerce.app.entity.Role;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.exception.BadRequestException;
import com.ecommerce.app.repository.CartRepository;
import com.ecommerce.app.repository.UserRepository;
import com.ecommerce.app.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager, JwtService jwtService) {
    this.userRepository = userRepository;
    this.cartRepository = cartRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new BadRequestException("Email already exists");
    }
    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.USER);
    User saved = userRepository.save(user);

    Cart cart = new Cart();
    cart.setUser(saved);
    cartRepository.save(cart);

    String token = jwtService.generateToken(saved.getEmail(), saved.getRole().name());
    return new AuthResponse(token, saved.getEmail(), saved.getRole().name());
  }

  public AuthResponse login(LoginRequest request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new BadRequestException("Invalid credentials"));
    String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
    return new AuthResponse(token, user.getEmail(), user.getRole().name());
  }
}

