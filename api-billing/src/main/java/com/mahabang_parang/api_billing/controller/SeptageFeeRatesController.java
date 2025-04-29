package com.mahabang_parang.api_billing.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_billing.model.ApiResponse;
import com.mahabang_parang.api_billing.model.dto.SeptageFeeRateDto;
import com.mahabang_parang.api_billing.service.SeptageFeeRateService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/billing/septage-fee")
public class SeptageFeeRatesController {

     @Autowired
     private SeptageFeeRateService septageService;
     
     @GetMapping("rates")
     public ResponseEntity<ApiResponse> getRates() {

         ApiResponse response = new ApiResponse();
          try {
               SeptageFeeRateDto septageRates = this.septageService.getRates();
               
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(septageRates);
               return ResponseEntity.status(HttpStatus.OK).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     @PostMapping("rates")
     public ResponseEntity<ApiResponse> updateRates(@RequestBody SeptageFeeRateDto payload) {
          ApiResponse response = new ApiResponse();

          try {
               SeptageFeeRateDto septageRates = this.septageService.update(payload);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(septageRates);
               return ResponseEntity.status(HttpStatus.OK).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     

}
