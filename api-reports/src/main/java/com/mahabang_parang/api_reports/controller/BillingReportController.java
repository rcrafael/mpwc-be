package com.mahabang_parang.api_reports.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_reports.service.BillingReportService;

@RestController
@RequestMapping("/api/reports/billing")
public class BillingReportController {

    @Autowired
    private BillingReportService billingReportService;

    @GetMapping("generateBillingReportSummary/{startDate}/{endDate}")
    public ResponseEntity<byte[]> generateBillingReportSummary(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer branchCode,
            @RequestParam(required = false) Integer phase) {
        try {
            byte[] fileContent = billingReportService.generateBillingReportSummary(startDate, endDate, branchCode, phase);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Billing_Report_Summary.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("generateBillingReportDetailed/{startDate}/{endDate}")
    public ResponseEntity<byte[]> generateBillingReportDetailed(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) Integer branchCode,
            @RequestParam(required = false) Integer phase) {
        try {
            byte[] fileContent = billingReportService.generateBillingReportDetailed(startDate, endDate, branchCode, phase);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=Billing_Report_Detailed.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
