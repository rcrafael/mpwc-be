package com.mahabang_parang.api_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.dto.RoleDto;
import com.mahabang_parang.api_user.service.RoleService;

@RestController
@RequestMapping("api/role")
public class RoleController {

     @Autowired
     private RoleService roleService;

     @GetMapping()
     public ResponseEntity<ApiResponse> getAllRoles() {
          ApiResponse response = new ApiResponse();

          try {
               List<RoleDto> roles = roleService.findAll();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(roles);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

}
