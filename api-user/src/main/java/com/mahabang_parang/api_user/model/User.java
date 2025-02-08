package com.mahabang_parang.api_user.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
     
     private UUID id;
     private String email;
     private String password;

}
