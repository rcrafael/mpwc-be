package com.mahabang_parang.api_user.service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.payload.LoginPayload;

import com.mahabang_parang.api_user.model.response.LoginResponse;

public interface AuthService {
     
     public LoginResponse login(LoginPayload payload) throws Exception;
     public String encodePassword(String password) throws Exception;
     
     // public UserDto create(UserDto payload) throws Exception;
}
