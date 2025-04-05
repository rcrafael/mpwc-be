package com.mahabang_parang.api_user.security;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.mahabang_parang.api_user.model.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
@Component
public class JwtUtil {

     private String SECRET_KEY = "TWFiYWhhbmdQYXJhbmdXYXRlcldvcmtzQ29vcGVyYXRpdmVXZWJBcHBsaWNhdGlvblNlY3JldEtleQ=="; //base64 encoded

     public String generateToken(UserEntity userDetails) {
          return Jwts.builder()
                         .subject(userDetails.getEmail())
                         .claim("email", userDetails.getEmail())
                         .claim("username", userDetails.getUsername())
                         .issuedAt(new Date(System.currentTimeMillis()))
                         .expiration(Date.from(Instant.now().plus(Duration.of(12, ChronoUnit.HOURS))))
                         .signWith(getSigningKey(), Jwts.SIG.HS256)
                         .compact();
     }

     public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
     }

     public Date extractExpiration(String token) {
          return extractClaim(token, Claims::getExpiration);
     }

     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
          final Claims claims = Jwts.parser()
                  .verifyWith(getSigningKey())
                  .build()
                  .parseSignedClaims(token)
                  .getPayload();
          return claimsResolver.apply(claims);
      }

     public boolean isValidToken(String token, UserDetails userDetails) {
          try {
               final String username = extractUsername(token);
               return (username.equals(userDetails.getUsername())) && !extractExpiration(token).before(new Date());
          } catch (ExpiredJwtException ex) {
               throw new ExpiredJwtException(ex.getHeader(), ex.getClaims(), "Token has expired");
          } catch (JwtException | IllegalArgumentException ex) {
               throw new BadCredentialsException("Invalid token");
          }
     }

     private SecretKey getSigningKey() {
          byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
         return new SecretKeySpec(keyBytes, "HmacSHA256");
        //    return Keys.hmacShaKeyFor(keyBytes);
    }

}
