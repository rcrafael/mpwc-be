package com.mahabang_parang.api_user.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(BadCredentialsException.class)
     public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
          Map<String, Object> body = new LinkedHashMap<>();
          body.put("status", HttpStatus.UNAUTHORIZED.value());
          body.put("message", "Invalid email or password");
          body.put("result", null);
          
          return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
     }

     @ExceptionHandler(InternalAuthenticationServiceException.class)
     public ResponseEntity<Object> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
          Map<String, Object> body = new LinkedHashMap<>();
          body.put("status", HttpStatus.UNAUTHORIZED.value());
          body.put("message", "Authentication failed: " + ex.getCause().getMessage());
          body.put("result", null);
          
          return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
     }

     @ExceptionHandler(value = {ExpiredJwtException.class})
     public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
          Map<String, Object> body = new LinkedHashMap<>();
          body.put("statusCode", HttpStatus.FORBIDDEN.value());
          body.put("message", "Token has expired");
          body.put("result", null);
          
          return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
     }
     

     @ExceptionHandler(value = {JwtException.class})
     public ResponseEntity<Object> handleJwtException(JwtException ex) {
          Map<String, Object> body = new LinkedHashMap<>();
          body.put("statusCode", HttpStatus.FORBIDDEN.value());
          body.put("message", "Invalid token");
          body.put("result", null);
          
          return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
     }
}
