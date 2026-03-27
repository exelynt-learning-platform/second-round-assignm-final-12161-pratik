package com.ecommerce.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
  @Value("${app.jwt.secret}")
  private String secret;
  @Value("${app.jwt.expirationMillis}")
  private long expirationMillis;

  private Key key;

  @PostConstruct
  void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String username, String role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);
    return Jwts.builder()
        .setSubject(username)
        .claim("role", role)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public boolean isTokenValid(String token, String username) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject().equals(username) && claims.getExpiration().after(new Date());
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }
}

