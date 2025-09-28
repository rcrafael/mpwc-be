package com.mahabang_parang.api_payment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_billing.model.ApiResponse;
import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.payload.OtherPaymentPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;
import com.mahabang_parang.api_payment.model.response.OtherPaymentResponse;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/payment/others")
public class OtherPaymentController {

     @Autowired
     private OfficialReceiptService orService; 


     @PostMapping()
     public ResponseEntity<ApiResponse> create(@RequestBody OtherPaymentPayload payload) throws Exception {

          System.out.println("OTHER PAYMENT CREATE API TRIGGERED!");

          ApiResponse response = new ApiResponse();
          try {
               OtherPaymentDto otherPayment = orService.createOtherCollection(payload);
               response.setResult(otherPayment);
               response.setMessage("success");
               response.setStatusCode(HttpStatus.CREATED);
               return ResponseEntity.ok(response);
          }
          catch( Error err) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + err.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getAllOtherPayment(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OtherPaymentHeaderDto> orHeader =  orService.getOtherCollectionAll(pageNo, pageSize, "entryDate");
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

     @GetMapping("with-cust-info/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getAllOtherPaymentWithCustInfo(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OtherPaymentHeaderWithCustomerDto> orHeader =  orService.getOtherCollectionWithCustomer(pageNo, pageSize, "entryDate");
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


     @GetMapping("with-cust-info/{id}")
     public ResponseEntity<ApiResponse> getOtherPaymentWithCustInfo(@PathVariable String id) {
         
          ApiResponse response = new ApiResponse();

          try {
               OtherPaymentResponse orHeader =  orService.getOtherCollectionWithDetailsById(id);
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
     public ResponseEntity<ApiResponse> getOtherPaymentByCustomerId(@PathVariable int pageNo, @PathVariable int pageSize, @PathVariable String customerId) {
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OtherPaymentHeaderDto> orHeader =  orService.getOtherCollectionByCustomerId(pageNo, pageSize, customerId);
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

      @GetMapping("search/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getOrWithSearch(@RequestParam String searchKeyword, @PathVariable int pageNo, @PathVariable int pageSize) {
         ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OfficialReceiptHeaderDto> OR = orService.getOtherCollectionAllWithSearch(searchKeyword, pageNo, pageSize);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(OR);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     


}
