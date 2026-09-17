package com.mahabang_parang.api_reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mahabang_parang.api_reports.model.interfaces.BillingReportDetailedProjection;
import com.mahabang_parang.api_reports.model.interfaces.BillingReportSummaryProjection;
import com.mahabang_parang.api_reports.repository.BillingReportRepository;
import com.mahabang_parang.api_reports.service.impl.BillingReportServiceImpl;
import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.service.BranchService;
import com.mahabang_parang.api_user.service.PhaseService;

@ExtendWith(MockitoExtension.class)
public class BillingReportServiceImplTest {

    @Mock
    private BillingReportRepository billingReportRepo;

    @Mock
    private BranchService branchService;

    @Mock
    private PhaseService phaseService;

    private BillingReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BillingReportServiceImpl(billingReportRepo, branchService, phaseService);
    }

    private BillingReportSummaryProjection createProjection(
            String readingNo, String accountNo, String meterNo, String customerName,
            String alias, Integer branchCode, Integer phase, String address,
            Integer monthOf, Date dateFrom, Date dateTo, Date dueDate,
            Integer m3Used, Double amount) {
        return new BillingReportSummaryProjection() {
            @Override public String getReadingNo() { return readingNo; }
            @Override public String getAccountNo() { return accountNo; }
            @Override public String getMeterNo() { return meterNo; }
            @Override public String getCustomerName() { return customerName; }
            @Override public String getAlias() { return alias; }
            @Override public Integer getBranchCode() { return branchCode; }
            @Override public Integer getPhase() { return phase; }
            @Override public String getAddress() { return address; }
            @Override public Integer getMonthOf() { return monthOf; }
            @Override public Date getDateFrom() { return dateFrom; }
            @Override public Date getDateTo() { return dateTo; }
            @Override public Date getDueDate() { return dueDate; }
            @Override public Integer getM3Used() { return m3Used; }
            @Override public Double getAmount() { return amount; }
        };
    }

    @Test
    void testGenerateBillingReportSummary_withoutFilters() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.JANUARY, 1, 0, 0, 0);
        Date dateFrom = cal.getTime();
        cal.set(2026, Calendar.JANUARY, 31, 23, 59, 59);
        Date dateTo = cal.getTime();
        cal.set(2026, Calendar.FEBRUARY, 15, 0, 0, 0);
        Date dueDate = cal.getTime();

        Date start = dateFrom;
        Date end = dateTo;

        BillingReportSummaryProjection record1 = createProjection(
                "RD-001", "ACC-101", "MTR-201", "Dela Cruz, Juan M",
                "JD", 1, 2, "123 Main St",
                1, dateFrom, dateTo, dueDate,
                25, 450.50
        );

        BillingReportSummaryProjection record2 = createProjection(
                "RD-002", "ACC-102", "MTR-202", "Santos, Maria",
                "MS", null, null, "456 Side St",
                1, dateFrom, dateTo, dueDate,
                15, 300.00
        );

        when(billingReportRepo.getBillingReportSummary(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(List.of(record1, record2));
        when(branchService.findAll()).thenReturn(List.of(new BranchDto(1, "BR-MAIN")));
        when(phaseService.findAll()).thenReturn(List.of(new PhaseDto(2, "Phase 2")));

        byte[] result = service.generateBillingReportSummary(start, end, null, null);
        assertNotNull(result);
        assertTrue(result.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Billing Report Summary");
            assertNotNull(sheet);

            // Row 0: Report Name
            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Billing Report Summary", r0.getCell(0).getStringCellValue());

            // Row 1: Report Date
            Row r1 = sheet.getRow(1);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            assertEquals("Report Date: " + sdf.format(start) + " to " + sdf.format(end), r1.getCell(0).getStringCellValue());

            // Row 2: Branch & Phase filter
            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: All   |   Phase: All", r2.getCell(0).getStringCellValue());

            // Row 4: Headers
            Row headerRow = sheet.getRow(4);
            assertEquals("Reading No", headerRow.getCell(0).getStringCellValue());
            assertEquals("Account No", headerRow.getCell(1).getStringCellValue());
            assertEquals("Meter No", headerRow.getCell(2).getStringCellValue());
            assertEquals("Customer Name", headerRow.getCell(3).getStringCellValue());
            assertEquals("Alias", headerRow.getCell(4).getStringCellValue());
            assertEquals("Branch Code", headerRow.getCell(5).getStringCellValue());
            assertEquals("Phase", headerRow.getCell(6).getStringCellValue());
            assertEquals("Address", headerRow.getCell(7).getStringCellValue());
            assertEquals("Month", headerRow.getCell(8).getStringCellValue());
            assertEquals("Billing Period", headerRow.getCell(9).getStringCellValue());
            assertEquals("Due Date", headerRow.getCell(10).getStringCellValue());
            assertEquals("M3 Used", headerRow.getCell(11).getStringCellValue());
            assertEquals("Amount", headerRow.getCell(12).getStringCellValue());

            // Row 5: Data record 1
            Row dataRow1 = sheet.getRow(5);
            assertEquals("RD-001", dataRow1.getCell(0).getStringCellValue());
            assertEquals("ACC-101", dataRow1.getCell(1).getStringCellValue());
            assertEquals("MTR-201", dataRow1.getCell(2).getStringCellValue());
            assertEquals("Dela Cruz, Juan M", dataRow1.getCell(3).getStringCellValue());
            assertEquals("JD", dataRow1.getCell(4).getStringCellValue());
            assertEquals("BR-MAIN", dataRow1.getCell(5).getStringCellValue());
            assertEquals("Phase 2", dataRow1.getCell(6).getStringCellValue());
            assertEquals("123 Main St", dataRow1.getCell(7).getStringCellValue());
            assertEquals("Jan", dataRow1.getCell(8).getStringCellValue());
            assertEquals(sdf.format(dateFrom) + " - " + sdf.format(dateTo), dataRow1.getCell(9).getStringCellValue());
            assertEquals(25.0, dataRow1.getCell(11).getNumericCellValue(), 0.001);
            assertEquals(450.50, dataRow1.getCell(12).getNumericCellValue(), 0.001);

            // Row 6: Data record 2 (null branch & phase)
            Row dataRow2 = sheet.getRow(6);
            assertEquals("RD-002", dataRow2.getCell(0).getStringCellValue());
            assertEquals("", dataRow2.getCell(5).getStringCellValue());
            assertEquals("", dataRow2.getCell(6).getStringCellValue());

            // Row 7: Totals row
            Row totalRow = sheet.getRow(7);
            assertEquals("TOTAL:", totalRow.getCell(10).getStringCellValue());
            assertEquals(40.0, totalRow.getCell(11).getNumericCellValue(), 0.001);
            assertEquals(750.50, totalRow.getCell(12).getNumericCellValue(), 0.001);
        }

        verify(billingReportRepo).getBillingReportSummary(eq(start), any(Date.class), eq(-1), eq(-1));
    }

    @Test
    void testGenerateBillingReportSummary_withBranchAndPhaseFilters() throws Exception {
        Date start = new Date();
        Date end = new Date();

        when(billingReportRepo.getBillingReportSummary(eq(start), any(Date.class), eq(1), eq(2)))
                .thenReturn(Collections.emptyList());
        when(branchService.findAll()).thenReturn(List.of(new BranchDto(1, "BR-01")));
        when(phaseService.findAll()).thenReturn(List.of(new PhaseDto(2, "Phase 2")));

        byte[] result = service.generateBillingReportSummary(start, end, 1, 2);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Billing Report Summary");
            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: BR-01   |   Phase: Phase 2", r2.getCell(0).getStringCellValue());
        }

        verify(billingReportRepo).getBillingReportSummary(eq(start), any(Date.class), eq(1), eq(2));
    }

    private BillingReportDetailedProjection createDetailedProjection(
            String readingNo, String accountNo, String meterNo, String customerName,
            String alias, Integer branchCode, Integer phase, String address,
            Integer monthOf, Date dateFrom, Date dateTo, Date dueDate,
            Integer previous, Integer present, Integer m3Used,
            Double readingAmount, Double parts, Double maintenanceFee,
            Double septageFee, Double lessAmount, Double totalAmount) {
        return new BillingReportDetailedProjection() {
            @Override public String getReadingNo() { return readingNo; }
            @Override public String getAccountNo() { return accountNo; }
            @Override public String getMeterNo() { return meterNo; }
            @Override public String getCustomerName() { return customerName; }
            @Override public String getAlias() { return alias; }
            @Override public Integer getBranchCode() { return branchCode; }
            @Override public Integer getPhase() { return phase; }
            @Override public String getAddress() { return address; }
            @Override public Integer getMonthOf() { return monthOf; }
            @Override public Date getDateFrom() { return dateFrom; }
            @Override public Date getDateTo() { return dateTo; }
            @Override public Date getDueDate() { return dueDate; }
            @Override public Integer getPrevious() { return previous; }
            @Override public Integer getPresent() { return present; }
            @Override public Integer getM3Used() { return m3Used; }
            @Override public Double getReadingAmount() { return readingAmount; }
            @Override public Double getParts() { return parts; }
            @Override public Double getMaintenanceFee() { return maintenanceFee; }
            @Override public Double getSeptageFee() { return septageFee; }
            @Override public Double getLessAmount() { return lessAmount; }
            @Override public Double getTotalAmount() { return totalAmount; }
        };
    }

    @Test
    void testGenerateBillingReportDetailed_success() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2026, Calendar.MARCH, 1, 0, 0, 0);
        Date dateFrom = cal.getTime();
        cal.set(2026, Calendar.MARCH, 31, 23, 59, 59);
        Date dateTo = cal.getTime();
        cal.set(2026, Calendar.APRIL, 15, 0, 0, 0);
        Date dueDate = cal.getTime();

        Date start = dateFrom;
        Date end = dateTo;

        BillingReportDetailedProjection record = createDetailedProjection(
                "RD-100", "ACC-200", "MTR-300", "Reyes, Pedro S",
                "PR", 1, 2, "789 North Ave",
                3, dateFrom, dateTo, dueDate,
                100, 120, 20,
                350.00, 50.00, 20.00, 30.00, 10.00, 440.00
        );

        when(billingReportRepo.getBillingReportDetailed(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(List.of(record));
        when(branchService.findAll()).thenReturn(List.of(new BranchDto(1, "BR-NORTH")));
        when(phaseService.findAll()).thenReturn(List.of(new PhaseDto(2, "Phase 2")));

        byte[] result = service.generateBillingReportDetailed(start, end, null, null);
        assertNotNull(result);
        assertTrue(result.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Billing Report Detailed");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Billing Report Detailed", r0.getCell(0).getStringCellValue());

            Row r1 = sheet.getRow(1);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            assertEquals("Report Date: " + sdf.format(start) + " to " + sdf.format(end), r1.getCell(0).getStringCellValue());

            Row headerRow = sheet.getRow(4);
            assertEquals("Reading No", headerRow.getCell(0).getStringCellValue());
            assertEquals("Account No", headerRow.getCell(1).getStringCellValue());
            assertEquals("Meter No", headerRow.getCell(2).getStringCellValue());
            assertEquals("Customer Name", headerRow.getCell(3).getStringCellValue());
            assertEquals("Alias", headerRow.getCell(4).getStringCellValue());
            assertEquals("Branch Code", headerRow.getCell(5).getStringCellValue());
            assertEquals("Phase", headerRow.getCell(6).getStringCellValue());
            assertEquals("Address", headerRow.getCell(7).getStringCellValue());
            assertEquals("Month", headerRow.getCell(8).getStringCellValue());
            assertEquals("Billing Period", headerRow.getCell(9).getStringCellValue());
            assertEquals("Due Date", headerRow.getCell(10).getStringCellValue());
            assertEquals("Previous M3", headerRow.getCell(11).getStringCellValue());
            assertEquals("Present M3", headerRow.getCell(12).getStringCellValue());
            assertEquals("M3 Used", headerRow.getCell(13).getStringCellValue());
            assertEquals("Reading Amount", headerRow.getCell(14).getStringCellValue());
            assertEquals("Parts", headerRow.getCell(15).getStringCellValue());
            assertEquals("Maintenance Fee", headerRow.getCell(16).getStringCellValue());
            assertEquals("Septage Fee", headerRow.getCell(17).getStringCellValue());
            assertEquals("Less", headerRow.getCell(18).getStringCellValue());
            assertEquals("Total Amount", headerRow.getCell(19).getStringCellValue());

            Row dataRow = sheet.getRow(5);
            assertEquals("RD-100", dataRow.getCell(0).getStringCellValue());
            assertEquals("ACC-200", dataRow.getCell(1).getStringCellValue());
            assertEquals("MTR-300", dataRow.getCell(2).getStringCellValue());
            assertEquals("Reyes, Pedro S", dataRow.getCell(3).getStringCellValue());
            assertEquals("PR", dataRow.getCell(4).getStringCellValue());
            assertEquals("BR-NORTH", dataRow.getCell(5).getStringCellValue());
            assertEquals("Phase 2", dataRow.getCell(6).getStringCellValue());
            assertEquals("789 North Ave", dataRow.getCell(7).getStringCellValue());
            assertEquals("Mar", dataRow.getCell(8).getStringCellValue());
            assertEquals(sdf.format(dateFrom) + " - " + sdf.format(dateTo), dataRow.getCell(9).getStringCellValue());
            assertEquals(100.0, dataRow.getCell(11).getNumericCellValue(), 0.001);
            assertEquals(120.0, dataRow.getCell(12).getNumericCellValue(), 0.001);
            assertEquals(20.0, dataRow.getCell(13).getNumericCellValue(), 0.001);
            assertEquals(350.00, dataRow.getCell(14).getNumericCellValue(), 0.001);
            assertEquals(50.00, dataRow.getCell(15).getNumericCellValue(), 0.001);
            assertEquals(20.00, dataRow.getCell(16).getNumericCellValue(), 0.001);
            assertEquals(30.00, dataRow.getCell(17).getNumericCellValue(), 0.001);
            assertEquals(10.00, dataRow.getCell(18).getNumericCellValue(), 0.001);
            assertEquals(440.00, dataRow.getCell(19).getNumericCellValue(), 0.001);

            Row totalRow = sheet.getRow(6);
            assertEquals("TOTAL:", totalRow.getCell(10).getStringCellValue());
            assertEquals(20.0, totalRow.getCell(13).getNumericCellValue(), 0.001);
            assertEquals(350.00, totalRow.getCell(14).getNumericCellValue(), 0.001);
            assertEquals(50.00, totalRow.getCell(15).getNumericCellValue(), 0.001);
            assertEquals(20.00, totalRow.getCell(16).getNumericCellValue(), 0.001);
            assertEquals(30.00, totalRow.getCell(17).getNumericCellValue(), 0.001);
            assertEquals(10.00, totalRow.getCell(18).getNumericCellValue(), 0.001);
            assertEquals(440.00, totalRow.getCell(19).getNumericCellValue(), 0.001);
        }

        verify(billingReportRepo).getBillingReportDetailed(eq(start), any(Date.class), eq(-1), eq(-1));
    }
}
