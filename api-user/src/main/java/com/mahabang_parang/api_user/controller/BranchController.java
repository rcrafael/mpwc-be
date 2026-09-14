package com.mahabang_parang.api_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.service.BranchService;

@RestController
@RequestMapping("api/branch")
public class BranchController {

     @Autowired
     private BranchService branchService;

     @GetMapping()
     public ResponseEntity<ApiResponse> getAllBranches() {
          ApiResponse response = new ApiResponse();
          try {
               List<BranchDto> branches = branchService.findAll();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(branches);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("/{id}")
     public ResponseEntity<ApiResponse> getBranchById(@PathVariable Integer id) {
          ApiResponse response = new ApiResponse();
          try {
               BranchDto branch = branchService.findById(id);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(branch);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
}
