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
import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.service.PhaseService;

@RestController
@RequestMapping("api/phase")
public class PhaseController {

     @Autowired
     private PhaseService phaseService;

     @GetMapping()
     public ResponseEntity<ApiResponse> getAllPhases() {
          ApiResponse response = new ApiResponse();
          try {
               List<PhaseDto> phases = phaseService.findAll();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(phases);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("/{id}")
     public ResponseEntity<ApiResponse> getPhaseById(@PathVariable Integer id) {
          ApiResponse response = new ApiResponse();
          try {
               PhaseDto phase = phaseService.findById(id);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(phase);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
}
