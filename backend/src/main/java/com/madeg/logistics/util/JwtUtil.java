package com.madeg.logistics.util;

import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.Role;
import com.madeg.logistics.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class JwtUtil {

  @Value("${security.jwt.token.key}")
  private String secretKey;

  @Value("${security.jwt.access.expire.minute}")
  private int accessExpireMinute;

  @Value("${security.jwt.refresh.expire.minute}")
  private int refreshExpireMinute;

  private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
  private Key signingKey;
  private final UserRepository userRepository;

  public JwtUtil(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostConstruct
  public void init() {
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
    this.signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
  }

  public String generateAccessToken(String username, Role userRole) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", userRole);
    return createToken(claims, username, 1000 * 60 * accessExpireMinute);
  }

  public String generateRefreshToken(String username) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, username, 1000 * 60 * refreshExpireMinute);
  }

  private String createToken(Map<String, Object> claims, String subject, long validity) {
    return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + validity))
        .signWith(signingKey, signatureAlgorithm)
        .compact();
  }

  public String refreshAccessToken(String refreshToken) {
    if (!validateRefreshToken(refreshToken)) {
      throw new IllegalArgumentException("유효하지 않은 refresh token");
    }
    String username = extractUsername(refreshToken);
    User user = userRepository.findByUsername(username);
    return generateAccessToken(username, user.getRole());
  }

  public boolean validateRefreshToken(String token) {
    return !isTokenExpired(token);
  }

  public boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(signingKey)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String resolveToken(HttpServletRequest request) {
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(
        request);
    String token = wrappedRequest.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
    }
    return token;
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = loadUserByUsername(
        extractUsername(token));
    return new UsernamePasswordAuthenticationToken(
        userDetails,
        "",
        userDetails.getAuthorities());
  }

  private UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(
          String.format("User %s not found", username));
    }
    return user;
  }
}
