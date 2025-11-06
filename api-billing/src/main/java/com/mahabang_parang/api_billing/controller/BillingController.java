package com.mahabang_parang.api_billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_billing.model.ApiResponse;
import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.dto.BillingWithCustomerInfoDto;
import com.mahabang_parang.api_billing.model.payload.UpdateBillingPayload;
import com.mahabang_parang.api_billing.model.response.BillingTableListResponse;
import com.mahabang_parang.api_billing.service.BillingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("api/billing")
public class BillingController {

     @Autowired
     private BillingService billingSvc;


     @GetMapping("/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getBilling(@PathVariable int pageNo, 
                                                  @PathVariable int pageSize) throws Exception {
          ApiResponse response = new ApiResponse();

          try {
               System.out.println("===============================================================================");
               System.out.println("GET BILLING API TRIGGERED!");
               System.out.println("===============================================================================");
               PaginationResponse<BillingTableListResponse> billing = billingSvc.getBilling(pageNo,pageSize);
               
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("customer/{customerId}/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getBillingByCustomer(@PathVariable String customerId,
                                                  @PathVariable int pageNo, 
                                                  @PathVariable int pageSize) throws Exception {
          
          ApiResponse response = new ApiResponse();
          try {
               PaginationResponse<BillingDto> billing = billingSvc.getBillingByCustId(customerId,pageNo,pageSize);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("id/{billingId}")
     public ResponseEntity<ApiResponse> getBillingById(@PathVariable String billingId) throws Exception {
          
          ApiResponse response = new ApiResponse();
          try {
               BillingWithCustomerInfoDto billing = billingSvc.getBillingWithCustomerInfoById(billingId);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     
     @PostMapping()
     public ResponseEntity<ApiResponse> createBilling(@RequestBody BillingDto billing) throws Exception {
          ApiResponse response = new ApiResponse();
          try {
               BillingDto bill = billingSvc.create(billing);
               response.setStatusCode(HttpStatus.CREATED);
               response.setMessage("success");
               response.setResult(bill );
               return ResponseEntity.status(HttpStatus.CREATED).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("new-reading-number")
     public ResponseEntity<ApiResponse> genrateReadingNo() {
          ApiResponse response = new ApiResponse();

          try {
               String accountNo = billingSvc.generateReadingNo();

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(accountNo);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     @GetMapping("get-recent-billings")
     public ResponseEntity<ApiResponse> getRecentBillings(@RequestParam() String customerId) {
          ApiResponse response = new ApiResponse();
          try {
               List<BillingDto> billing = billingSvc.getRecentBillingByCustomerId(customerId);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("get-unpaid-bills")
     public ResponseEntity<ApiResponse> getUnpaidBillingByCustomerId(@RequestParam String customerId) {
          ApiResponse response = new ApiResponse();
          try {
               List<BillingDto> billing = billingSvc.getUnpaidBillingByCustomerId(customerId);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     
     @PutMapping()
     public ResponseEntity<ApiResponse> update(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateBillingPayload payload) {
          ApiResponse response = new ApiResponse();
          try {
               BillingDto bill = billingSvc.update(payload);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(bill );
               return ResponseEntity.status(HttpStatus.OK).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("search/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> searchBilling(@RequestParam String keyword, @PathVariable int pageNo, @PathVariable int pageSize) {
         ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<BillingTableListResponse> billing = billingSvc.searchBilling(keyword, pageNo, pageSize);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(billing);
               return ResponseEntity.status(HttpStatus.OK).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }
     


}
