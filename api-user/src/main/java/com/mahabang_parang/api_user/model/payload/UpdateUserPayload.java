package com.mahabang_parang.api_user.model.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateUserPayload {

     private String id;
     private String username;
     private String email;
     private String password;
     private String fname;
     private String lname;
     private String mname;
     private String roleId;
     private java.util.List<String> pageAccess;

}
