package com.mahabang_parang.api_user.service;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.payload.LoginPayload;

public interface AuthService {
     
     public String login(LoginPayload payload) throws Exception;
     public String encodePassword(String password) throws Exception;
     
     // public UserDto create(UserDto payload) throws Exception;
}
