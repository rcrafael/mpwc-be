package com.mahabang_parang.api_user.service;

import com.mahabang_parang.api_user.model.dto.UserDto;

public interface UserService {

     public UserDto findByEmail(String email);
     public UserDto findById(String id);

}
