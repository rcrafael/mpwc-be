package com.mahabang_parang.api_billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_billing.model.ApiResponse;
import com.mahabang_parang.api_billing.model.dto.WaterRatesDto;
import com.mahabang_parang.api_billing.service.WaterRatesService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/billing/rates")
public class WaterRatesController {

     @Autowired
     private WaterRatesService ratesSvc;

     @PostMapping("commercial")
     public  ResponseEntity<ApiResponse> addCommercialRates(@RequestBody List<WaterRatesDto> payload) {
         
          ApiResponse response = new ApiResponse();
          try {
               List<WaterRatesDto> waterRates = this.ratesSvc.addWaterRatesCommercial(payload);
               
               response.setStatusCode(HttpStatus.CREATED);
               response.setMessage("success");
               response.setResult(waterRates);
               return ResponseEntity.status(HttpStatus.CREATED).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @PostMapping("residential")
     public  ResponseEntity<ApiResponse> addResidentialRates(@RequestBody List<WaterRatesDto> payload) {
         
          System.out.println("-------------------------------------------------------------------------------");
          System.out.println("PAYLOAD CONTENT: " + payload.toString());
          System.out.println("-------------------------------------------------------------------------------");


          ApiResponse response = new ApiResponse();
          try {
               List<WaterRatesDto> waterRates = this.ratesSvc.addWaterRatesResidentials(payload);
               
               response.setStatusCode(HttpStatus.CREATED);
               response.setMessage("success");
               response.setResult(waterRates);
               return ResponseEntity.status(HttpStatus.CREATED).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }
     
     @GetMapping("commercial/active")
     public  ResponseEntity<ApiResponse> getActiveCommercialRates() {
         
          ApiResponse response = new ApiResponse();
          try {
               List<WaterRatesDto> waterRates = this.ratesSvc.getActiveWaterRatesCommercial();
               
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(waterRates);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("residential/active")
     public  ResponseEntity<ApiResponse> getActiveResidentialRates() {
         
          ApiResponse response = new ApiResponse();
          try {
               List<WaterRatesDto> waterRates = this.ratesSvc.getActiveWaterRatesResidentials();
               
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(waterRates);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }
}
