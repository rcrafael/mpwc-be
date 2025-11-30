package com.mahabang_parang.api_payment.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahabang_parang.api_payment.model.ApiResponse;
import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.model.payload.UpdateStatusPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptTableListResponse;
import com.mahabang_parang.api_payment.service.OfficialReceiptService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/payment/official-receipt")
public class OfficialReceiptController {

     @Autowired
     private OfficialReceiptService orService;

     @GetMapping("/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getAllOR(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OfficialReceiptHeaderDto> orHeader =  orService.getOfficialReceiptAll(pageNo, pageSize, "entryDate");
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
     public ResponseEntity<ApiResponse> getAllORWithCustInfo(@PathVariable int pageNo, @PathVariable int pageSize) {
         
          ApiResponse response = new ApiResponse();

          try {
               PaginationResponse<OfficialReceiptTableListResponse> orHeader =  orService.getOfficialReceiptWithCustomer(pageNo, pageSize, "entryDate");
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
     public ResponseEntity<ApiResponse> getORWithCustInfo(@PathVariable String id) {
         
          ApiResponse response = new ApiResponse();

          try {
               OfficialReceiptResponse orHeader =  orService.getOfficialReceiptWithDetailsById(id);
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
     
     @DeleteMapping("{id}")
     public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
          ApiResponse response = new ApiResponse();

          try {
               String result = orService.deleteRecord(id);
               response.setResult(result);
               response.setStatusCode(HttpStatus.OK);
               response.setMessage("success");
               return ResponseEntity.ok(response);
          }
          catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          }
     }

     @PostMapping("update-status")
     public ResponseEntity<ApiResponse> updateStatus(@RequestBody UpdateStatusPayload payload) {
         ApiResponse response = new ApiResponse();

          try {

               OfficialReceiptHeaderDto orHeader =  orService.updateRecordStatus(payload);
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
               PaginationResponse<OfficialReceiptTableListResponse> OR = orService.getOfficialReceiptWithSearch(searchKeyword, pageNo, pageSize);

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
     
     @GetMapping("receipt/excel/{id}")
     public ResponseEntity<?> generateExcelReport(@PathVariable String id) throws Exception {
          try {
               byte[] excelBytes = orService.printExcelOR(id);
               
               HttpHeaders headers = new HttpHeaders();
               headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
               headers.setContentDisposition(
                    ContentDisposition.attachment()
                    .filename("report_" + id + ".xlsx")
                    .build()
               );
               headers.setContentLength(excelBytes.length);
               
               return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);
          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body(e.getMessage());
          }

     }

     @GetMapping("receipt/pdf/{id}")
    public ResponseEntity<?> generatePdfReport(@PathVariable() String id) throws Exception {
        try {
            byte[] pdfBytes = orService.printPdfOR(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename("report_" + id + ".pdf")
                    .build()
            );
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
     
    @DeleteMapping("void/{id}")
    public ResponseEntity<ApiResponse> voidRecord(@PathVariable String id) {
        
     ApiResponse response = new ApiResponse();

     try {
          OfficialReceiptHeaderDto result = orService.voidRecord(id);
          response.setResult(result);
          response.setStatusCode(HttpStatus.OK);
          response.setMessage("success");
          return ResponseEntity.ok(response);
     }
     catch (Exception e) {
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
