package com.mahabang_parang.api_user.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.payload.LoginPayload;
import com.mahabang_parang.api_user.service.AuthService;

@RestController
@RequestMapping("api/auth")
public class AuthController {

     @Autowired
     private AuthService authService;
     
     @PostMapping("login")
     public ResponseEntity<ApiResponse> login(@RequestBody LoginPayload payload) {
          ApiResponse response = new ApiResponse();

          try {
               String token = authService.login(payload);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(Map.of("accessToken", token));
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @PostMapping("encodePassword")
     public ResponseEntity<ApiResponse> encodePassword(@RequestParam String password) {
          ApiResponse response = new ApiResponse();

          try {
               String pw = authService.encodePassword(password);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(Map.of("password", pw));
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     

}
