package com.mahabang_parang.api_reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mahabang_parang.api_reports.controller.BillingReportController;
import com.mahabang_parang.api_reports.service.BillingReportService;

@ExtendWith(MockitoExtension.class)
public class BillingReportControllerTest {

    @Mock
    private BillingReportService billingReportService;

    @InjectMocks
    private BillingReportController controller;

    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() {
        startDate = new Date();
        endDate = new Date();
    }

    @Test
    void testGenerateBillingReportSummary_success() {
        byte[] dummyContent = new byte[]{1, 2, 3, 4};
        when(billingReportService.generateBillingReportSummary(eq(startDate), eq(endDate), eq(1), eq(2)))
                .thenReturn(dummyContent);

        ResponseEntity<byte[]> response = controller.generateBillingReportSummary(startDate, endDate, 1, 2);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyContent, response.getBody());
        assertEquals("attachment; filename=Billing_Report_Summary.xlsx",
                response.getHeaders().getFirst("Content-Disposition"));
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                response.getHeaders().getFirst("Content-Type"));

        verify(billingReportService).generateBillingReportSummary(eq(startDate), eq(endDate), eq(1), eq(2));
    }

    @Test
    void testGenerateBillingReportSummary_error() {
        when(billingReportService.generateBillingReportSummary(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<byte[]> response = controller.generateBillingReportSummary(startDate, endDate, null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGenerateBillingReportDetailed_success() {
        byte[] dummyContent = new byte[]{5, 6, 7, 8};
        when(billingReportService.generateBillingReportDetailed(eq(startDate), eq(endDate), eq(1), eq(2)))
                .thenReturn(dummyContent);

        ResponseEntity<byte[]> response = controller.generateBillingReportDetailed(startDate, endDate, 1, 2);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyContent, response.getBody());
        assertEquals("attachment; filename=Billing_Report_Detailed.xlsx",
                response.getHeaders().getFirst("Content-Disposition"));
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                response.getHeaders().getFirst("Content-Type"));

        verify(billingReportService).generateBillingReportDetailed(eq(startDate), eq(endDate), eq(1), eq(2));
    }

    @Test
    void testGenerateBillingReportDetailed_error() {
        when(billingReportService.generateBillingReportDetailed(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<byte[]> response = controller.generateBillingReportDetailed(startDate, endDate, null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGenerateUnpaidBillingReport_success() {
        byte[] dummyContent = new byte[]{9, 10, 11, 12};
        when(billingReportService.generateUnpaidBillingReport(eq(startDate), eq(endDate), eq(1), eq(2)))
                .thenReturn(dummyContent);

        ResponseEntity<byte[]> response = controller.generateUnpaidBillingReport(startDate, endDate, 1, 2);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dummyContent, response.getBody());
        assertEquals("attachment; filename=Unpaid_Billing_Report.xlsx",
                response.getHeaders().getFirst("Content-Disposition"));
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                response.getHeaders().getFirst("Content-Type"));

        verify(billingReportService).generateUnpaidBillingReport(eq(startDate), eq(endDate), eq(1), eq(2));
    }

    @Test
    void testGenerateUnpaidBillingReport_error() {
        when(billingReportService.generateUnpaidBillingReport(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<byte[]> response = controller.generateUnpaidBillingReport(startDate, endDate, null, null);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
