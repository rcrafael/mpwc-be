package com.mahabang_parang.api_payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahabang_parang.api_payment.model.ApiResponse;
import com.mahabang_parang.api_payment.model.dto.ShareSummaryDto;
import com.mahabang_parang.api_payment.service.ShareSummaryService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/payment/share-summary")
public class ShareSummaryController {

     private final ShareSummaryService _shareSummaryService;

     public ShareSummaryController(ShareSummaryService shareSummaryService) {
          this._shareSummaryService = shareSummaryService;
     }

     @GetMapping("/{customerId}")
     public ResponseEntity<ApiResponse> getByCustomerId(@PathVariable String customerId) {

          ApiResponse response = new ApiResponse();
          try {
               ShareSummaryDto result = _shareSummaryService.getByCustomerId(customerId);
               response.setResult(result);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               return ResponseEntity.ok(response);     
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @PostMapping("update")
     public ResponseEntity<ApiResponse> update(@RequestBody ShareSummaryDto params, HttpServletRequest request) {
         
          Map<String, Object> decodeJwt = requestMapper(request);
          String userId = (String) decodeJwt.get("id");

          if(params.getId() == null) {
               params.setCreatedBy(userId);
          } else {
               params.setUpdatedBy(userId);
          }

          ApiResponse response = new ApiResponse();
          try {
               ShareSummaryDto result = _shareSummaryService.createOrUpdate(params);
               response.setResult(result);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     private Map<String, Object> requestMapper(HttpServletRequest request) {
          try {
               String auth = request.getHeader("Authorization");
               String token = auth.substring(7);

               String[] tokenParts = token.split("\\.");
               String payload = "";

               if(tokenParts.length > 0) {
                    payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));
               }

               ObjectMapper mapper = new ObjectMapper();
               return mapper.readValue(payload, Map.class);
          } catch (Exception e) {
               throw new RuntimeException("Failed to decode token: " + e.getMessage(), e); 
          }
     }

}
