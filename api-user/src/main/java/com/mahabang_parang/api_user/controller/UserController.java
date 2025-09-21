package com.mahabang_parang.api_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.service.UserService;

@RestController
@RequestMapping("api/user")
public class UserController {

     @Autowired
     private UserService userService;

     @GetMapping()
     public ResponseEntity<ApiResponse> getUserById(@RequestParam String id) {
          ApiResponse response = new ApiResponse();

           try {
               UserDto user = this.userService.findById(id);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(user);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("username/{id}")
     public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String id) {
          ApiResponse response = new ApiResponse();

          try {
               String user = this.userService.getUsername(id);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(user);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

}
