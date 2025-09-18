package com.mahabang_parang.api_settings.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_settings.model.ApiResponse;
import com.mahabang_parang.api_settings.model.PaginationResponse;
import com.mahabang_parang.api_settings.model.dto.PartDto;
import com.mahabang_parang.api_settings.model.dto.PartsListingDto;
import com.mahabang_parang.api_settings.service.PartService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/settings/part")
public class PartController {

     @Autowired
     private PartService partService;



     @GetMapping("{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getAll(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

           try {
               PaginationResponse<PartDto> parts =  partService.getAll(pageNo, pageSize);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(parts);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("list-all")
     public ResponseEntity<ApiResponse> listParts() {
         
          ApiResponse response = new ApiResponse();

           try {
               List<PartsListingDto> parts =  partService.listParts();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(parts);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping()
     public ResponseEntity<ApiResponse> findById(@RequestParam String id) {
          ApiResponse response = new ApiResponse();

          try {
              PartDto part =  partService.findById(id);
              response.setStatusCode(HttpStatus.OK);
              response.setMessage("success");
              response.setResult(part);
              return ResponseEntity.ok(response);

         } catch (Exception e) {
              response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
              response.setMessage("Internal Server Error: " + e.getMessage());
              
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }
     
     
     @PostMapping()
     public ResponseEntity<ApiResponse> create(@RequestBody PartDto payload) {
          ApiResponse response = new ApiResponse();

          try {
              PartDto part =  partService.create(payload);
              response.setStatusCode(HttpStatus.CREATED);
              response.setMessage("success");
              response.setResult(part);
              return ResponseEntity.status(HttpStatus.CREATED).body(response);

         } catch (Exception e) {
              response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
              response.setMessage("Internal Server Error: " + e.getMessage());
              
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }
     

}
