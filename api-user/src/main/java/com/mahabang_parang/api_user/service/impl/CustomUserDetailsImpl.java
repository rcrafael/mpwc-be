package com.mahabang_parang.api_user.service.impl;

import java.util.Collection;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsImpl implements UserDetailsService {

     private final UserService userService;

     @Override
     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          UserDto user = new UserDto();
          try {
               user = this.userService.findByEmail(email);
          } catch (UsernameNotFoundException e) {
               System.out.println("USERNAME NOT FOUND: " + email);
               throw new UsernameNotFoundException("USERNAME NOT FOUND: " + email);
          }

          
          return User.withUsername(user.getEmail()) //used email as a username
                    .password(user.getPassword())
                    .roles("ADMIN")
                    .build();

     }



}
