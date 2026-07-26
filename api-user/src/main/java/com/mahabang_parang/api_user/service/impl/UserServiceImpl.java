package com.mahabang_parang.api_user.service.impl;

import com.mahabang_parang.api_user.ApiUserApplication;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.entity.RoleEntity;
import com.mahabang_parang.api_user.model.entity.UserEntity;
import com.mahabang_parang.api_user.model.mapper.UserMapper;
import com.mahabang_parang.api_user.model.payload.RegisterPayload;
import com.mahabang_parang.api_user.model.payload.UpdateUserPayload;
import com.mahabang_parang.api_user.repository.RoleRepository;
import com.mahabang_parang.api_user.repository.UserRepository;
import com.mahabang_parang.api_user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;

     private final RoleRepository roleRepository;

     private final UserMapper mapper;

     @Lazy
     private final PasswordEncoder passwordEncoder;

     UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
          this.userRepository = userRepository;
          this.roleRepository = roleRepository;
          this.mapper = mapper;
          this.passwordEncoder = passwordEncoder;
     }

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

     @Override
     public String getUsername(String id) throws Exception {
          System.out.println("USER ID: " + id);
          UserEntity entity = userRepository.findById(id).orElse(null);
          return entity != null ? entity.getUsername() : "";
     }

     @Override
     public UserDto register(RegisterPayload payload) throws Exception {
          RoleEntity role = roleRepository.findById(payload.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + payload.getRoleId()));

          System.out.println("ROLE:" + role.toString());

          UserEntity user = new UserEntity();
          user.setUsername(payload.getUsername());
          user.setEmail(payload.getEmail());
          user.setPassword(passwordEncoder.encode(payload.getPassword()));
          user.setFname(payload.getFname());
          user.setLname(payload.getLname());
          user.setMname(payload.getMname());
          user.setRole(role);

          UserEntity saved = userRepository.save(user);
          return mapper.toDto(saved);
     }

     public List<UserDto> getAllUsers() {
          List<UserEntity> users = userRepository.findAll();
          return mapper.toList(users);
     }


     @Override
     public UserDto updateUser(UpdateUserPayload payload) throws Exception {
          
          UserEntity user = userRepository.findById(payload.getId()).orElseThrow();

          if(user == null) {
               throw new RuntimeException("User not found with id: " + payload.getId());
          }

          // check every field if contains value

          if(payload.getEmail() != null && !payload.getPassword().isEmpty()) {
               user.setEmail(payload.getEmail());
          }

          if(payload.getPassword() != null && !payload.getPassword().isEmpty()) {
               user.setPassword(passwordEncoder.encode(payload.getPassword()));
          }

          if(payload.getFname() != null && !payload.getFname().isEmpty()) {
               user.setFname(payload.getFname());
          }

          if(payload.getLname() != null && !payload.getLname().isEmpty()) {
               user.setLname(payload.getLname());
          }

          if(payload.getMname() != null && !payload.getMname().isEmpty()) {
               user.setMname(payload.getMname());
          }

          if(payload.getRoleId() != null && !payload.getRoleId().isEmpty()) {
               RoleEntity role = roleRepository.findById(payload.getRoleId())
                         .orElseThrow(() -> new RuntimeException("Role not found with id: " + payload.getRoleId()));
               user.setRole(role);
          }

          UserEntity saved = userRepository.save(user);
          return mapper.toDto(saved);

     }

}
