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

import org.springframework.transaction.annotation.Transactional;
import com.mahabang_parang.api_user.repository.UserPageAccessRepository;

@Service
public class UserServiceImpl implements UserService {

     private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final UserPageAccessRepository userPageAccessRepository;
     private final UserMapper mapper;

     @Lazy
     private final PasswordEncoder passwordEncoder;

     UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserPageAccessRepository userPageAccessRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
          this.userRepository = userRepository;
          this.roleRepository = roleRepository;
          this.userPageAccessRepository = userPageAccessRepository;
          this.mapper = mapper;
          this.passwordEncoder = passwordEncoder;
     }

     @Override
     public UserDto findByEmail(String email) {
          UserDto user = mapper.toDto(userRepository.findByEmail(email));
          return user;
     }

     @Override
     public UserEntity findEntityByEmail(String email) {
          return userRepository.findByEmail(email);
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
     @Transactional(rollbackFor = Exception.class)
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

          List<String> pagesToAssign = payload.getPageAccess();
          String roleName = role.getRole() != null ? role.getRole() : "";

          // If role is superadmin or admin and no explicit pages provided, default to all pages
          if ((pagesToAssign == null || pagesToAssign.isEmpty()) && 
              ("superadmin".equalsIgnoreCase(roleName) || "admin".equalsIgnoreCase(roleName))) {
               pagesToAssign = com.mahabang_parang.api_user.model.constant.PageConstants.ALL_PAGES;
          }

          if (pagesToAssign != null && !pagesToAssign.isEmpty()) {
               for (String pageCode : pagesToAssign) {
                    if (pageCode != null && !pageCode.trim().isEmpty()) {
                         user.getPageAccess().add(
                              com.mahabang_parang.api_user.model.entity.UserPageAccessEntity.builder()
                                   .user(user)
                                   .pageCode(pageCode.trim())
                                   .build()
                         );
                    }
               }
          }

          UserEntity saved = userRepository.save(user);
          return mapper.toDto(saved);
     }

     public List<UserDto> getAllUsers() {
          List<UserEntity> users = userRepository.findAll();
          return mapper.toList(users);
     }

     @Override
     public List<java.util.Map<String, String>> getPageDefinitions() {
          return com.mahabang_parang.api_user.model.constant.PageConstants.ALL_PAGE_DEFINITIONS;
     }

     @Override
     @Transactional(rollbackFor = Exception.class)
     public UserDto updateUser(UpdateUserPayload payload) throws Exception {
          
          UserEntity user = userRepository.findById(payload.getId()).orElseThrow();

          if(user == null) {
               throw new RuntimeException("User not found with id: " + payload.getId());
          }

          // check every field if contains value

          if(payload.getEmail() != null && !payload.getEmail().isEmpty()) {
               System.out.println("UPDATE EMAIL: " + payload.getEmail());
               user.setEmail(payload.getEmail());
          }

          if(payload.getPassword() != null && !payload.getPassword().isEmpty()) {
               System.out.println("UPDATE Password: " + passwordEncoder.encode(payload.getPassword()));
               user.setPassword(passwordEncoder.encode(payload.getPassword()));
          }

          if(payload.getFname() != null && !payload.getFname().isEmpty()) {
               System.out.println("UPDATE Fname: " + payload.getFname());
               user.setFname(payload.getFname());
          }

          if(payload.getLname() != null && !payload.getLname().isEmpty()) {
               System.out.println("UPDATE Lname: " + payload.getLname());
               user.setLname(payload.getLname());
          }

          if(payload.getMname() != null && !payload.getMname().isEmpty()) {
               System.out.println("UPDATE Mname: " + payload.getMname());
               user.setMname(payload.getMname());
          }

          if(payload.getRoleId() != null && !payload.getRoleId().isEmpty()) {
               System.out.println("UPDATE RoleId: " + payload.getRoleId());
               RoleEntity role = roleRepository.findById(payload.getRoleId())
                         .orElseThrow(() -> new RuntimeException("Role not found with id: " + payload.getRoleId()));
               user.setRole(role);
          }

          if(payload.getPageAccess() != null) {
               System.out.println("UPDATE PageAccess: " + payload.getPageAccess());
               
               java.util.Set<String> newPageCodes = payload.getPageAccess().stream()
                         .filter(code -> code != null && !code.trim().isEmpty())
                         .map(String::trim)
                         .collect(java.util.stream.Collectors.toSet());

               // Remove permissions that are not in the new list (orphanRemoval handles DELETEs)
               user.getPageAccess().removeIf(existing -> !newPageCodes.contains(existing.getPageCode()));

               // Find existing page codes remaining
               java.util.Set<String> existingCodes = user.getPageAccess().stream()
                         .map(com.mahabang_parang.api_user.model.entity.UserPageAccessEntity::getPageCode)
                         .collect(java.util.stream.Collectors.toSet());

               // Add only genuinely new page codes (no duplicate inserts for existing rows)
               for (String code : newPageCodes) {
                    if (!existingCodes.contains(code)) {
                         user.getPageAccess().add(
                              com.mahabang_parang.api_user.model.entity.UserPageAccessEntity.builder()
                                   .user(user)
                                   .pageCode(code)
                                   .build()
                         );
                    }
               }
          }

          UserEntity saved = userRepository.save(user);
          return mapper.toDto(saved);

     }

}
