package com.mahabang_parang.api_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.PaginationResponse;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.model.payload.UpdateMeterStatusPayload;
import com.mahabang_parang.api_user.service.CustomerMeterService;
import com.mahabang_parang.api_user.service.CustomerService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("api/customer")
public class CustomerController {

     private final CustomerService custService;
     private final CustomerMeterService meterService;

     CustomerController(CustomerService custService, CustomerMeterService meterService) {
          this.custService = custService;
          this.meterService = meterService;
     }
     @GetMapping("get/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getCustomers(@PathVariable int pageNo, @PathVariable int pageSize) {
          
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<CustomerDto> customers = this.custService.getCustomers(pageNo,pageSize);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }

     @GetMapping("{id}")
     public ResponseEntity<ApiResponse> getCustomer(@PathVariable() String id) {
          ApiResponse response = new ApiResponse();

          try {
               CustomerDto customer = this.custService.getCustomer(id);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customer);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }  

     @GetMapping("first-name")
     public ResponseEntity<ApiResponse> getCustomerbyFirstname(@RequestParam String firstName) {

         ApiResponse response = new ApiResponse();

          try {
               List<CustomerDto> customers = custService.getCustomersByFirstName(firstName);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("search/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getCustomerWithSearch(@RequestParam String searchKeyword, @PathVariable int pageNo, @PathVariable int pageSize) {
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<CustomerDto> customers = custService.getCustomerWithSearch(searchKeyword, pageNo, pageSize);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     
     @PostMapping()
     public ResponseEntity<ApiResponse> addCustomer(@RequestBody CustomerDto customer) {
          
          ApiResponse response = new ApiResponse();

          try {
               CustomerDto customers = custService.addCustomer(customer);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
         
     }
     
     @PutMapping()
     public ResponseEntity<ApiResponse> updateCustomer(@RequestBody CustomerDto customer) {
          ApiResponse response = new ApiResponse();

          try {
               CustomerDto customers = custService.updateCustomer(customer);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @GetMapping("new-account-number")
     public ResponseEntity<ApiResponse> generateAccountNo() {
          ApiResponse response = new ApiResponse();

          try {
               String accountNo = custService.generateAccountNo();

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
     

     @PostMapping("meter")
     public ResponseEntity<ApiResponse> addMeter(@RequestBody CustomerMeterDto payload) {
          
          System.out.println("payload: " + payload.toString());

          ApiResponse response = new ApiResponse();

          try {
               response = meterService.addCustomerMeter(payload);

               if(response.getStatusCode() == HttpStatus.CONFLICT) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, response.getMessage());
               }

               return ResponseEntity.ok(response);
          } catch (ResponseStatusException e) {
               response.setStatusCode(HttpStatus.CONFLICT);
               response.setMessage(e.getMessage());
               
               return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }
     
     @GetMapping("meter/all/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getMeters(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               List<CustomerMeterDto> customers = this.meterService.getCustomerMeters();
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               System.out.println(e.getMessage() + " : " + e.getLocalizedMessage() + " : " + e.getCause() );
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }

     }
     
     @GetMapping("meter")
     public ResponseEntity<ApiResponse> getMeterbyCustomer(@RequestParam String customerId) {
         
          ApiResponse response = new ApiResponse();

          try {
               List<CustomerMeterDto> customers = this.meterService.getCustomerMetersByCustomerId(customerId);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customers);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }
     
     @GetMapping("meter/{meterNo}")
     public ResponseEntity<ApiResponse> getMeterByMeterNo(@PathVariable String meterNo) {
         
          ApiResponse response = new ApiResponse();

          try {
               CustomerMeterDto customerMeter = this.meterService.getCustomerMeterByMeterNo(meterNo);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(customerMeter);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @PutMapping("meter/single-update")
     public ResponseEntity<ApiResponse> updateMeterNo(@RequestBody CustomerMeterDto meter){
          
          ApiResponse response = new ApiResponse();

          try {
               CustomerMeterDto mtr = meterService.updateCustomerMeter(meter);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(mtr);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }


     @PutMapping("meter/status-update") 
     public ResponseEntity<ApiResponse> updateMeterStaus(@RequestBody UpdateMeterStatusPayload payload) {
          ApiResponse response = new ApiResponse();

          try {
               CustomerMeterDto mtr = meterService.updateCustomerMeterStatus(payload);

               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               response.setResult(mtr);
               return ResponseEntity.ok(response);
          } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }


}
