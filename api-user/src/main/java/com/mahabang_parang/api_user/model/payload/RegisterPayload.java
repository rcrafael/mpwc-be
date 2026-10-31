package com.mahabang_parang.api_user.model.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RegisterPayload {

     private String username;
     private String email;
     private String password;
     private String fname;
     private String lname;
     private String mname;

     /** The ID of the role to assign to the new user. */
     private String roleId;

     private java.util.List<String> pageAccess;

}
