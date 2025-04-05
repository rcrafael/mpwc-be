package com.mahabang_parang.api_user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.mapper.UserMapper;
import com.mahabang_parang.api_user.model.payload.LoginPayload;
import com.mahabang_parang.api_user.security.JwtUtil;
import com.mahabang_parang.api_user.service.AuthService;
import com.mahabang_parang.api_user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

     private final UserService userService;
     private final PasswordEncoder passwordEncoder;
     private final JwtUtil jwtUtils;
     private final AuthenticationManager authManager;
     private final UserMapper mapper;

     public String login(LoginPayload payload) throws Exception {
          System.out.println("LOGIN API TRIGGERED!");

          System.out.println("PAYLOAD : "  + payload.toString());

          authManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getEmail(), payload.getPassword()));
          System.out.println("AUTHENTICATED! ");
          UserDto user = userService.findByEmail(payload.getEmail());

          System.out.println("USER: " + user.toString());
          if(user != null && passwordEncoder.matches(payload.getPassword(), user.getPassword()) ) {
               return jwtUtils.generateToken(mapper.toEntity(user));
          }
          throw new RuntimeException("Invalid Username or Password!");
     }

     public String encodePassword(String password) throws Exception {
          return passwordEncoder.encode(password);
     }


     // public UserDto create(UserDto user) throws Exception {

     // }

     

}
