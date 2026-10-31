package com.mahabang_parang.api_user.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mahabang_parang.api_user.model.entity.RoleEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto {

     private String id;
     
     private String email;

     private String username;

     private String fname;
     private String lname;
     private String mname;

     @JsonIgnore
     private String password;

     private RoleEntity role;

     private java.util.List<String> pageAccess = new java.util.ArrayList<>();

}
