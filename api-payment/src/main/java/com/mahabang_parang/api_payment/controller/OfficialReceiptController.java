package com.mahabang_parang.api_payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_payment.model.ApiResponse;
import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/payment/official-receipt")
public class OfficialReceiptController {

     @Autowired
     private OfficialReceiptService orService;


     @GetMapping("/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getAllOR(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OfficialReceiptHeaderDto> orHeader =  orService.getOfficialReceiptAll(pageNo, pageSize, "orDate");
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(orHeader);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("customer/{customerId}/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getORByCustomerId(@PathVariable int pageNo, @PathVariable int pageSize, @PathVariable String customerId) {
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OfficialReceiptHeaderDto> orHeader =  orService.getOfficialReceiptByCustomerId(pageNo, pageSize, customerId);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(orHeader);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     @GetMapping("new-or-number")
     public ResponseEntity<ApiResponse> generateORNo() {
          ApiResponse response = new ApiResponse();

          try {
               String orHeader =  orService.generateOfficialReceiptNo();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(orHeader);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }


     @PostMapping()
     public ResponseEntity<ApiResponse> create(@RequestBody OfficiaReceiptPayload payload) {
         
          ApiResponse response = new ApiResponse();

          try {

               OfficialReceiptDto orHeader =  orService.createOfficialReceipt(payload);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(orHeader);
               return ResponseEntity.ok(response);

          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     

}
