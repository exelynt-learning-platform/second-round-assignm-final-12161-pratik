package com.ecommerce.app.service;

import com.ecommerce.app.dto.auth.LoginRequest;
import com.ecommerce.app.dto.auth.RegisterRequest;
import com.ecommerce.app.entity.Role;
import com.ecommerce.app.entity.User;
import com.ecommerce.app.repository.CartRepository;
import com.ecommerce.app.repository.UserRepository;
import com.ecommerce.app.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private CartRepository cartRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;
  @InjectMocks private AuthService authService;

  @Test
  void register_shouldCreateUserAndReturnToken() {
    RegisterRequest req = new RegisterRequest();
    req.setName("Test");
    req.setEmail("t@example.com");
    req.setPassword("secret123");
    when(userRepository.existsByEmail(req.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(any())).thenReturn("hashed");
    User saved = new User(); saved.setId(1L); saved.setEmail(req.getEmail()); saved.setRole(Role.USER);
    when(userRepository.save(any(User.class))).thenReturn(saved);
    when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt");

    String token = authService.register(req).getToken();
    assertEquals("jwt", token);
    verify(cartRepository, times(1)).save(any());
  }

  @Test
  void login_shouldAuthenticateAndReturnToken() {
    LoginRequest req = new LoginRequest();
    req.setEmail("a@a.com");
    req.setPassword("p");
    User user = new User(); user.setEmail(req.getEmail()); user.setRole(Role.USER);
    when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(anyString(), anyString())).thenReturn("jwt");

    String token = authService.login(req).getToken();
    assertEquals("jwt", token);
    verify(authenticationManager, times(1))
        .authenticate(any(UsernamePasswordAuthenticationToken.class));
  }
}

