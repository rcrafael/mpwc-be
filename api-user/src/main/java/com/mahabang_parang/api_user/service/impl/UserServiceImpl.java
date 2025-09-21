package com.mahabang_parang.api_user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.entity.UserEntity;
import com.mahabang_parang.api_user.model.mapper.UserMapper;
import com.mahabang_parang.api_user.repository.UserRepository;
import com.mahabang_parang.api_user.service.UserService;

@Service
public class UserServiceImpl implements UserService{

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private UserMapper mapper;

     @Autowired
     private UserRepository userRepo;

     @Override
     public UserDto findByEmail(String email) {
          UserDto user = mapper.toDto(userRepository.findByEmail(email));
          return user;
     }

     @Override
     public UserDto findById(String id) {
          UserDto user = mapper.toDto(userRepository.findById(id).orElseThrow());
          return user;
     }

     public String getUsername(String id) throws Exception {

          System.out.println("USER ID: " + id);

          UserEntity entity = userRepo.findById(id).orElse(null);
          return entity != null ? entity.getUsername() : "";
     }

     
}
