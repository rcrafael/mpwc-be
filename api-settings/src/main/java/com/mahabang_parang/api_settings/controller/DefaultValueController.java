package com.mahabang_parang.api_settings.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_settings.model.ApiResponse;
import com.mahabang_parang.api_settings.model.dto.DefaultValueDto;
import com.mahabang_parang.api_settings.service.DefaultValueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/settings/default-values")
public class DefaultValueController {

     @Autowired
     private DefaultValueService defaultValueService;

     @GetMapping()
     public ResponseEntity<ApiResponse> getDefaultValues() throws Exception {
          ApiResponse response = new ApiResponse();
          
          try {
               DefaultValueDto result = defaultValueService.getDefaultValue();
               response.setResult(result);
               response.setMessage("Success");
               response.setStatusCode(HttpStatus.OK);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
          
     }

     @PostMapping()
     public ResponseEntity<ApiResponse> updateDefaultValuResponseEntity(@RequestBody DefaultValueDto payload) {
         ApiResponse response = new ApiResponse();
          
          try {
               DefaultValueDto result = defaultValueService.updateDefaultValue(payload);
               response.setResult(result);
               response.setMessage("Success");
               response.setStatusCode(HttpStatus.OK);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     



}
