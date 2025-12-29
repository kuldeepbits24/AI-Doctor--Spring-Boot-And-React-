package com.jdoc.hospital.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtTokenService {

  private final SecretKey key;
  private final long expirationMillis;

  public JwtTokenService(@Value("${app.jwt.secret}") String secret,
                         @Value("${app.jwt.expirationMillis}") long expirationMillis) {
    if (secret.length() < 32) {
      throw new IllegalArgumentException("JWT secret must be at least 32 characters");
    }
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMillis = expirationMillis;
  }

  public String generateToken(CustomUserDetails userDetails) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("role", userDetails.getUser().getRole().name())
        .claim("userId", userDetails.getUser().getId())
        .issuedAt(now)
        .expiration(expiry)
        .signWith(key)
        .compact();
  }

  public Jws<Claims> parseToken(String token) throws JwtException {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token);
  }
}
