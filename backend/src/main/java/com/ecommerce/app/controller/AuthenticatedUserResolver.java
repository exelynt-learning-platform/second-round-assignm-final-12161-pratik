package com.ecommerce.app.controller;

import com.ecommerce.app.exception.UnauthorizedException;
import com.ecommerce.app.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserResolver {
  private final UserRepository userRepository;

  public AuthenticatedUserResolver(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Long getUserId(Authentication authentication) {
    if (authentication == null || authentication.getName() == null) {
      throw new UnauthorizedException("Unauthorized");
    }
    return userRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UnauthorizedException("User not found"))
        .getId();
  }
}

