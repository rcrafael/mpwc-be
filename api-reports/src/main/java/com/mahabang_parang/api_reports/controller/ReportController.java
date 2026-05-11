package com.mahabang_parang.api_reports.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_reports.model.ApiResponse;
import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.dto.DashboardCardDataDto;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;
import com.mahabang_parang.api_reports.service.CollectionReportService;

import java.util.Date;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

     @Autowired
     private CollectionReportService collectionReportService;

     @GetMapping("getShareCapCbuSavingsCollectionByCustomer/{customerId}")
     public ResponseEntity<ApiResponse> getShareCapCbuSavingsCollectionByCustomerId(@PathVariable String customerId) {
         ApiResponse response = new ApiResponse();
         try {
          
               ShareCapCbuSavingsCollectionInterfaceProjection result = collectionReportService.getShareCapCbuSavingsCollectionByCustomerId(customerId);
               response.setResult(result);
               response.setMessage("success");
               response.setStatusCode(HttpStatus.OK);
               return ResponseEntity.ok(response);
         } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }

     @GetMapping("getShareCapCbuSavingsCollectionByCustomerAndDateRange/{customerId}/{startDate}/{endDate}")
     public ResponseEntity<ApiResponse> getShareCapCbuSavingsCollectionByCustomerAndDateRange(
                                                @PathVariable String customerId, 
                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, 
                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
                                                      
         ApiResponse response = new ApiResponse();
         try {
               ShareCapCbuSavingsCollectionInterfaceProjection result = collectionReportService.getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(customerId, startDate, endDate);
               response.setResult(result);
               response.setMessage("success");
               response.setStatusCode(HttpStatus.OK);
               return ResponseEntity.ok(response);
         } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }

     @GetMapping("getShareCapCbuSavingsCollectionListByDateRange/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getShareCapCbuSavingsCollectionListByDateRange(
                                                @PathVariable int pageNo, 
                                                @PathVariable int pageSize, 
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

         ApiResponse response = new ApiResponse();
         try {
          
            PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                                          collectionReportService.getShareCapCbuSavingsCollectionListByDateRange(pageNo, pageSize, startDate, endDate);
            response.setResult(result);
            response.setMessage("success");
            response.setStatusCode(HttpStatus.OK);
            return ResponseEntity.ok(response);
         } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }

     @GetMapping("getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange/{pageNo}/{pageSize}")
     public ResponseEntity<ApiResponse> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                @PathVariable int pageNo, 
                                                @PathVariable int pageSize, 
                                                @RequestParam String customerId, 
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

         ApiResponse response = new ApiResponse();
         try {
          
            PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                                          collectionReportService.getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(pageNo, pageSize, customerId, startDate, endDate);
            response.setResult(result);
            response.setMessage("success");
            response.setStatusCode(HttpStatus.OK);
            return ResponseEntity.ok(response);
         } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }
     
     @GetMapping("getDashboardData/{startDate}/{endDate}")
     public ResponseEntity<ApiResponse> getDashboardData(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
         ApiResponse response = new ApiResponse();
         try {
          
            DashboardCardDataDto result = 
                                          collectionReportService.getDashboardCardData(startDate, endDate);
            response.setResult(result);
            response.setMessage("success");
            response.setStatusCode(HttpStatus.OK);
            return ResponseEntity.ok(response);
         } catch (Exception e) {
               response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
               response.setMessage("Internal Server Error: " + e.getMessage());
               
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
         }
     }
     
     @GetMapping("generateCollectionReportSummary/{startDate}/{endDate}")
     public ResponseEntity<byte[]> generateCollectionReport(
               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, 
               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
          try {
               byte[] fileContent = collectionReportService.generateCollectionReportExcel(startDate, endDate);
               
               org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
               headers.add("Content-Disposition", "attachment; filename=Collection_Report.xlsx");
               headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
               
               return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
          }
     }

     @GetMapping("generateCollectionReportDetails/{startDate}/{endDate}")
     public ResponseEntity<byte[]> generateCollectionReportDetails(
               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
               @RequestParam(required = false) Integer branchCode,
               @RequestParam(required = false) Integer phase) {
          try {
               byte[] fileContent = collectionReportService.generateCollectionReportDetails(startDate, endDate, branchCode, phase);

               org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
               headers.add("Content-Disposition", "attachment; filename=Collection_Report_Details.xlsx");
               headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

               return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
          }
     }

}
