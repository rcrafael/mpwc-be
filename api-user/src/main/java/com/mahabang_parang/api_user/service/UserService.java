package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.payload.RegisterPayload;
import com.mahabang_parang.api_user.model.payload.UpdateUserPayload;

public interface UserService {

     public UserDto findByEmail(String email);
     public UserDto findById(String id);
     public String getUsername(String id) throws Exception;
     public UserDto register(RegisterPayload payload) throws Exception;
     public UserDto updateUser(UpdateUserPayload payload) throws Exception;

     public List<UserDto> getAllUsers();

}
