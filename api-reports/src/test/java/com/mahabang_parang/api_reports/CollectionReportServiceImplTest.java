package com.mahabang_parang.api_reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.CollectionReportInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.OtherCollectionReportDetailProjection;
import com.mahabang_parang.api_reports.model.interfaces.OtherCollectionReportSummaryProjection;
import com.mahabang_parang.api_reports.model.interfaces.OutsideCollectionReportDetailProjection;
import com.mahabang_parang.api_reports.model.interfaces.OutsideCollectionReportSummaryProjection;
import com.mahabang_parang.api_reports.repository.ORCollectionRepository;
import com.mahabang_parang.api_reports.service.impl.CollectionReportServiceImpl;
import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.repository.CustomerRepository;
import com.mahabang_parang.api_user.service.BranchService;
import com.mahabang_parang.api_user.service.PhaseService;

@ExtendWith(MockitoExtension.class)
public class CollectionReportServiceImplTest {

    @Mock
    private ORCollectionRepository orCollectionRepo;
    @Mock
    private BillingRepository billingRepo;
    @Mock
    private CustomerRepository customerRepo;
    @Mock
    private CustomerMeterRepository customerMeterRepo;
    @Mock
    private OfficialReceiptHeaderRepository orHeaderRepo;
    @Mock
    private BranchService branchService;
    @Mock
    private PhaseService phaseService;

    private CollectionReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CollectionReportServiceImpl(
                orCollectionRepo,
                billingRepo,
                customerRepo,
                customerMeterRepo,
                orHeaderRepo,
                branchService,
                phaseService
        );
    }

    private CollectionReportInterfaceProjection createSummaryProjection(
            String orNo, Date orDate, String acctNo, String alias,
            String first, String mid, String last,
            Double billing, Double share, Double senior, Double totalPay) {
        return new CollectionReportInterfaceProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public Double getTotalBillingAmount() { return billing; }
            @Override public Double getTotalShareAmount() { return share; }
            @Override public Double getSeniorDiscount() { return senior; }
            @Override public Double getTotalAmountPay() { return totalPay; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerAlias() { return alias; }
            @Override public String getFirstName() { return first; }
            @Override public String getMiddleName() { return mid; }
            @Override public String getLastName() { return last; }
        };
    }

    private CollectionReportDetailInterfaceProjection createDetailProjection(
            String orNo, Date orDate, String acctNo, String alias,
            String first, String mid, String last,
            Double billing, Double share, Double senior, Double totalPay,
            String meterNo, Integer branchCode, Integer phase,
            Double readingPaid, Double parts, Double membership,
            Double tapping, Double calibration, Double recon,
            Double subRec, Double penalty, Double otherCol,
            String otherDesc, Double maintFee, Double detailSenior,
            Double septage, Double detailTotal, String remarks) {
        return new CollectionReportDetailInterfaceProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public Double getTotalBillingAmount() { return billing; }
            @Override public Double getTotalShareAmount() { return share; }
            @Override public Double getSeniorDiscount() { return senior; }
            @Override public Double getTotalAmountPay() { return totalPay; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerAlias() { return alias; }
            @Override public String getFirstName() { return first; }
            @Override public String getMiddleName() { return mid; }
            @Override public String getLastName() { return last; }
            @Override public String getMeterNo() { return meterNo; }
            @Override public Integer getBranchCode() { return branchCode; }
            @Override public Integer getPhase() { return phase; }
            @Override public Double getReadingAmountPaid() { return readingPaid; }
            @Override public Double getParts() { return parts; }
            @Override public Double getMembership() { return membership; }
            @Override public Double getTapping() { return tapping; }
            @Override public Double getCalibration() { return calibration; }
            @Override public Double getReconnection() { return recon; }
            @Override public Double getSubReceivable() { return subRec; }
            @Override public Double getPenalty() { return penalty; }
            @Override public Double getOtherCollection() { return otherCol; }
            @Override public String getOtherDescription() { return otherDesc; }
            @Override public Double getMaintenanceFee() { return maintFee; }
            @Override public Double getDetailSeniorDiscount() { return detailSenior; }
            @Override public Double getSeptageFee() { return septage; }
            @Override public Double getDetailTotalAmount() { return detailTotal; }
            @Override public String getRemarks() { return remarks; }
        };
    }

    @Test
    void testGenerateCollectionReportExcel_withoutFilters() throws Exception {
        Date start = new Date();
        Date end = new Date();

        CollectionReportInterfaceProjection record = createSummaryProjection(
                "OR-001", start, "ACC-01", "Alias1",
                "Juan", "D", "Cruz",
                500.0, 50.0, 10.0, 540.0
        );
        when(orCollectionRepo.getCollectionReportByDateRange(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(List.of(record));

        byte[] result = service.generateCollectionReportExcel(start, end, null, null);
        assertNotNull(result);
        assertTrue(result.length > 0);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Collection Report", r0.getCell(0).getStringCellValue());

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: All   |   Phase: All", r2.getCell(0).getStringCellValue());

            // Header row is row 4
            Row headerRow = sheet.getRow(4);
            assertEquals("OR No", headerRow.getCell(0).getStringCellValue());

            // Data row is row 5
            Row dataRow = sheet.getRow(5);
            assertEquals("OR-001", dataRow.getCell(0).getStringCellValue());
            assertEquals(500.0, dataRow.getCell(7).getNumericCellValue());
        }

        verify(orCollectionRepo).getCollectionReportByDateRange(eq(start), any(Date.class), eq(-1), eq(-1));
    }

    @Test
    void testGenerateCollectionReportExcel_withBranchCodeAndPhase() throws Exception {
        Date start = new Date();
        Date end = new Date();
        Integer branchCode = 101;
        Integer phase = 2;

        when(branchService.findAll()).thenReturn(List.of(new BranchDto(101, "Mahabang Parang")));
        when(phaseService.findAll()).thenReturn(List.of(new PhaseDto(2, "Phase 2")));

        CollectionReportInterfaceProjection record = createSummaryProjection(
                "OR-002", start, "ACC-02", "Alias2",
                "Maria", "S", "Santos",
                300.0, 30.0, 0.0, 330.0
        );
        when(orCollectionRepo.getCollectionReportByDateRange(eq(start), any(Date.class), eq(branchCode), eq(phase)))
                .thenReturn(List.of(record));

        byte[] result = service.generateCollectionReportExcel(start, end, branchCode, phase);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report");
            assertNotNull(sheet);

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: Mahabang Parang   |   Phase: Phase 2", r2.getCell(0).getStringCellValue());

            Row dataRow = sheet.getRow(5);
            assertEquals("OR-002", dataRow.getCell(0).getStringCellValue());
        }

        verify(orCollectionRepo).getCollectionReportByDateRange(eq(start), any(Date.class), eq(branchCode), eq(phase));
    }

    @Test
    void testGenerateCollectionReportDetails_withFilters() throws Exception {
        Date start = new Date();
        Date end = new Date();
        Integer branchCode = 101;
        Integer phase = 1;

        when(branchService.findAll()).thenReturn(List.of(new BranchDto(101, "Mahabang Parang")));
        when(phaseService.findAll()).thenReturn(List.of(new PhaseDto(1, "Phase 1")));

        CollectionReportDetailInterfaceProjection detail = createDetailProjection(
                "OR-001", start, "ACC-01", "Alias1",
                "Juan", "D", "Cruz",
                500.0, 50.0, 10.0, 540.0,
                "MTR-001", 101, 1,
                400.0, 0.0, 0.0,
                0.0, 0.0, 0.0,
                0.0, 0.0, 0.0,
                "", 50.0, 10.0,
                0.0, 440.0, "Test remarks"
        );

        when(orCollectionRepo.getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(branchCode), eq(phase)))
                .thenReturn(List.of(detail));

        byte[] result = service.generateCollectionReportDetails(start, end, branchCode, phase);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report Details");
            assertNotNull(sheet);

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: Mahabang Parang   |   Phase: Phase 1", r2.getCell(0).getStringCellValue());

            // Parent row is row 5
            Row parentRow = sheet.getRow(5);
            assertEquals("OR-001", parentRow.getCell(0).getStringCellValue());
            assertEquals(500.0, parentRow.getCell(7).getNumericCellValue());

            // Child row is row 6
            Row childRow = sheet.getRow(6);
            assertEquals("MTR-001", childRow.getCell(11).getStringCellValue());
            assertEquals("Mahabang Parang", childRow.getCell(12).getStringCellValue());
            assertEquals("Phase 1", childRow.getCell(13).getStringCellValue());
        }

        verify(orCollectionRepo).getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(branchCode), eq(phase));
    }

    @Test
    void testGenerateCollectionReportDetails_withoutFilters() throws Exception {
        Date start = new Date();
        Date end = new Date();

        when(orCollectionRepo.getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(Collections.emptyList());

        byte[] result = service.generateCollectionReportDetails(start, end, null, null);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report Details");
            assertNotNull(sheet);

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: All   |   Phase: All", r2.getCell(0).getStringCellValue());
        }

        verify(orCollectionRepo).getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(-1), eq(-1));
    }

    @Test
    void testGenerateCollectionReportExcel_withNegativeOneAsAll() throws Exception {
        Date start = new Date();
        Date end = new Date();

        when(orCollectionRepo.getCollectionReportByDateRange(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(Collections.emptyList());

        byte[] result = service.generateCollectionReportExcel(start, end, -1, -1);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report");
            assertNotNull(sheet);

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: All   |   Phase: All", r2.getCell(0).getStringCellValue());
        }

        verify(orCollectionRepo).getCollectionReportByDateRange(eq(start), any(Date.class), eq(-1), eq(-1));
    }

    @Test
    void testGenerateCollectionReportDetails_withNegativeOneAsAll() throws Exception {
        Date start = new Date();
        Date end = new Date();

        when(orCollectionRepo.getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(-1), eq(-1)))
                .thenReturn(Collections.emptyList());

        byte[] result = service.generateCollectionReportDetails(start, end, -1, -1);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Collection Report Details");
            assertNotNull(sheet);

            Row r2 = sheet.getRow(2);
            assertEquals("Branch Code: All   |   Phase: All", r2.getCell(0).getStringCellValue());
        }

        verify(orCollectionRepo).getCollectionReportDetailsByDateRange(eq(start), any(Date.class), eq(-1), eq(-1));
    }

    private OtherCollectionReportSummaryProjection createOtherSummaryProjection(
            String orNo, Date orDate, String acctNo, String custName, String address, Integer status, Double totalPay) {
        return new OtherCollectionReportSummaryProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerName() { return custName; }
            @Override public String getAddress() { return address; }
            @Override public Integer getStatus() { return status; }
            @Override public Double getTotalAmountPay() { return totalPay; }
        };
    }

    private OtherCollectionReportDetailProjection createOtherDetailProjection(
            String orNo, Date orDate, String acctNo, String custName, String address, Integer status, Double totalPay,
            String ordId, String description, String remarks, Double detailTotal) {
        return new OtherCollectionReportDetailProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerName() { return custName; }
            @Override public String getAddress() { return address; }
            @Override public Integer getStatus() { return status; }
            @Override public Double getTotalAmountPay() { return totalPay; }
            @Override public String getOrdId() { return ordId; }
            @Override public String getDescription() { return description; }
            @Override public String getRemarks() { return remarks; }
            @Override public Double getDetailTotalAmount() { return detailTotal; }
        };
    }

    @Test
    void testGenerateOtherCollectionReportSummary() throws Exception {
        Date start = new Date();
        Date end = new Date();

        OtherCollectionReportSummaryProjection item1 = createOtherSummaryProjection(
                "OR-101", start, "ACC-001", "Dela Cruz, Juan", "123 Main St", 1, 1500.0);
        OtherCollectionReportSummaryProjection item2 = createOtherSummaryProjection(
                "OR-102", start, "ACC-002", "Santos, Maria", "456 Oak St", 1, 2500.0);

        when(orCollectionRepo.getOtherCollectionSummaryReportByDateRange(eq(start), any(Date.class)))
                .thenReturn(List.of(item1, item2));

        byte[] result = service.generateOtherCollectionReportSummary(start, end);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Other Collection Summary");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Other Collection Summary Report", r0.getCell(0).getStringCellValue());

            // Header row (row 3)
            Row hdr = sheet.getRow(3);
            assertEquals("OR No.", hdr.getCell(0).getStringCellValue());
            assertEquals("OR Date", hdr.getCell(1).getStringCellValue());
            assertEquals("Account No", hdr.getCell(2).getStringCellValue());
            assertEquals("Customer Name", hdr.getCell(3).getStringCellValue());
            assertEquals("Address", hdr.getCell(4).getStringCellValue());
            assertEquals("Status", hdr.getCell(5).getStringCellValue());
            assertEquals("Total Amount To Pay", hdr.getCell(6).getStringCellValue());

            // Data rows (rows 4 and 5)
            Row row1 = sheet.getRow(4);
            assertEquals("OR-101", row1.getCell(0).getStringCellValue());
            assertEquals("ACC-001", row1.getCell(2).getStringCellValue());
            assertEquals("Dela Cruz, Juan", row1.getCell(3).getStringCellValue());
            assertEquals("123 Main St", row1.getCell(4).getStringCellValue());
            assertEquals("Complete", row1.getCell(5).getStringCellValue());
            assertEquals(1500.0, row1.getCell(6).getNumericCellValue());

            Row row2 = sheet.getRow(5);
            assertEquals("OR-102", row2.getCell(0).getStringCellValue());
            assertEquals("ACC-002", row2.getCell(2).getStringCellValue());
            assertEquals("Santos, Maria", row2.getCell(3).getStringCellValue());
            assertEquals("456 Oak St", row2.getCell(4).getStringCellValue());
            assertEquals("Complete", row2.getCell(5).getStringCellValue());
            assertEquals(2500.0, row2.getCell(6).getNumericCellValue());

            // Total row (row 6)
            Row totalRow = sheet.getRow(6);
            assertEquals("TOTAL:", totalRow.getCell(5).getStringCellValue());
            assertEquals(4000.0, totalRow.getCell(6).getNumericCellValue());
        }

        verify(orCollectionRepo).getOtherCollectionSummaryReportByDateRange(eq(start), any(Date.class));
    }

    @Test
    void testGenerateOtherCollectionReportDetails() throws Exception {
        Date start = new Date();
        Date end = new Date();

        OtherCollectionReportDetailProjection detail1 = createOtherDetailProjection(
                "OR-101", start, "ACC-001", "Dela Cruz, Juan", "123 Main St", 1, 1500.0,
                "ORD-1", "Water Pipe Fitting", "Installed on site", 1000.0);
        OtherCollectionReportDetailProjection detail2 = createOtherDetailProjection(
                "OR-101", start, "ACC-001", "Dela Cruz, Juan", "123 Main St", 1, 1500.0,
                "ORD-2", "Labor Charge", "Repairs done", 500.0);

        when(orCollectionRepo.getOtherCollectionDetailsReportByDateRange(eq(start), any(Date.class)))
                .thenReturn(List.of(detail1, detail2));

        byte[] result = service.generateOtherCollectionReportDetails(start, end);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Other Collection Details Report");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Other Collection Detailed Report", r0.getCell(0).getStringCellValue());

            // Header row (row 3)
            Row hdr = sheet.getRow(3);
            assertEquals("OR No.", hdr.getCell(0).getStringCellValue());
            assertEquals("OR Date", hdr.getCell(1).getStringCellValue());
            assertEquals("Account No", hdr.getCell(2).getStringCellValue());
            assertEquals("Customer Name", hdr.getCell(3).getStringCellValue());
            assertEquals("Address", hdr.getCell(4).getStringCellValue());
            assertEquals("Status", hdr.getCell(5).getStringCellValue());
            assertEquals("Total Amount To Pay", hdr.getCell(6).getStringCellValue());
            assertEquals("Description", hdr.getCell(7).getStringCellValue());
            assertEquals("Remarks", hdr.getCell(8).getStringCellValue());
            assertEquals("Total Amount", hdr.getCell(9).getStringCellValue());

            // Parent row (row 4)
            Row parentRow = sheet.getRow(4);
            assertEquals("OR-101", parentRow.getCell(0).getStringCellValue());
            assertEquals("ACC-001", parentRow.getCell(2).getStringCellValue());
            assertEquals("Dela Cruz, Juan", parentRow.getCell(3).getStringCellValue());
            assertEquals("123 Main St", parentRow.getCell(4).getStringCellValue());
            assertEquals("Complete", parentRow.getCell(5).getStringCellValue());
            assertEquals(1500.0, parentRow.getCell(6).getNumericCellValue());

            // Child rows (rows 5 and 6)
            Row childRow1 = sheet.getRow(5);
            assertEquals("", childRow1.getCell(0).getStringCellValue());
            assertEquals("Water Pipe Fitting", childRow1.getCell(7).getStringCellValue());
            assertEquals("Installed on site", childRow1.getCell(8).getStringCellValue());
            assertEquals(1000.0, childRow1.getCell(9).getNumericCellValue());

            Row childRow2 = sheet.getRow(6);
            assertEquals("", childRow2.getCell(0).getStringCellValue());
            assertEquals("Labor Charge", childRow2.getCell(7).getStringCellValue());
            assertEquals("Repairs done", childRow2.getCell(8).getStringCellValue());
            assertEquals(500.0, childRow2.getCell(9).getNumericCellValue());

            // Grand Total row (row 7)
            Row totalRow = sheet.getRow(7);
            assertEquals("GRAND TOTAL:", totalRow.getCell(5).getStringCellValue());
            assertEquals(1500.0, totalRow.getCell(6).getNumericCellValue());
            assertEquals(1500.0, totalRow.getCell(9).getNumericCellValue());
        }

        verify(orCollectionRepo).getOtherCollectionDetailsReportByDateRange(eq(start), any(Date.class));
    }

    private OutsideCollectionReportSummaryProjection createOutsideSummaryProjection(
            String orNo, Date orDate, String acctNo, String custName, String address, Integer status, Double totalPay) {
        return new OutsideCollectionReportSummaryProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerName() { return custName; }
            @Override public String getAddress() { return address; }
            @Override public Integer getStatus() { return status; }
            @Override public Double getTotalAmountPay() { return totalPay; }
        };
    }

    private OutsideCollectionReportDetailProjection createOutsideDetailProjection(
            String orNo, Date orDate, String acctNo, String custName, String address, Integer status, Double totalPay,
            String ordId, String description, String remarks, Double detailTotal) {
        return new OutsideCollectionReportDetailProjection() {
            @Override public String getOrNo() { return orNo; }
            @Override public Date getOrDate() { return orDate; }
            @Override public String getAccountNo() { return acctNo; }
            @Override public String getCustomerName() { return custName; }
            @Override public String getAddress() { return address; }
            @Override public Integer getStatus() { return status; }
            @Override public Double getTotalAmountPay() { return totalPay; }
            @Override public String getOrdId() { return ordId; }
            @Override public String getDescription() { return description; }
            @Override public String getRemarks() { return remarks; }
            @Override public Double getDetailTotalAmount() { return detailTotal; }
        };
    }

    @Test
    void testGenerateOutsideCollectionReportSummary() throws Exception {
        Date start = new Date();
        Date end = new Date();

        OutsideCollectionReportSummaryProjection item1 = createOutsideSummaryProjection(
                "OR-201", start, "", "Outside Customer A", "789 Pine St", 1, 3500.0);
        OutsideCollectionReportSummaryProjection item2 = createOutsideSummaryProjection(
                "OR-202", start, "ACC-099", "Outside Customer B", "101 Elm St", 1, 1200.0);

        when(orCollectionRepo.getOutsideCollectionSummaryReportByDateRange(eq(start), any(Date.class)))
                .thenReturn(List.of(item1, item2));

        byte[] result = service.generateOutsideCollectionReportSummary(start, end);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Outside Collection Summary");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Outside Collection Summary Report", r0.getCell(0).getStringCellValue());

            // Header row (row 3)
            Row hdr = sheet.getRow(3);
            assertEquals("OR No.", hdr.getCell(0).getStringCellValue());
            assertEquals("OR Date", hdr.getCell(1).getStringCellValue());
            assertEquals("Account No", hdr.getCell(2).getStringCellValue());
            assertEquals("Customer Name", hdr.getCell(3).getStringCellValue());
            assertEquals("Address", hdr.getCell(4).getStringCellValue());
            assertEquals("Status", hdr.getCell(5).getStringCellValue());
            assertEquals("Total Amount To Pay", hdr.getCell(6).getStringCellValue());

            // Data rows (rows 4 and 5)
            Row row1 = sheet.getRow(4);
            assertEquals("OR-201", row1.getCell(0).getStringCellValue());
            assertEquals("", row1.getCell(2).getStringCellValue());
            assertEquals("Outside Customer A", row1.getCell(3).getStringCellValue());
            assertEquals("789 Pine St", row1.getCell(4).getStringCellValue());
            assertEquals("Complete", row1.getCell(5).getStringCellValue());
            assertEquals(3500.0, row1.getCell(6).getNumericCellValue());

            Row row2 = sheet.getRow(5);
            assertEquals("OR-202", row2.getCell(0).getStringCellValue());
            assertEquals("ACC-099", row2.getCell(2).getStringCellValue());
            assertEquals("Outside Customer B", row2.getCell(3).getStringCellValue());
            assertEquals("101 Elm St", row2.getCell(4).getStringCellValue());
            assertEquals("Complete", row2.getCell(5).getStringCellValue());
            assertEquals(1200.0, row2.getCell(6).getNumericCellValue());

            // Total row (row 6)
            Row totalRow = sheet.getRow(6);
            assertEquals("TOTAL:", totalRow.getCell(5).getStringCellValue());
            assertEquals(4700.0, totalRow.getCell(6).getNumericCellValue());
        }

        verify(orCollectionRepo).getOutsideCollectionSummaryReportByDateRange(eq(start), any(Date.class));
    }

    @Test
    void testGenerateOutsideCollectionReportDetails() throws Exception {
        Date start = new Date();
        Date end = new Date();

        OutsideCollectionReportDetailProjection detail1 = createOutsideDetailProjection(
                "OR-201", start, "", "Outside Customer A", "789 Pine St", 1, 3500.0,
                "ORD-201-1", "External Water Connection", "Commercial site", 2500.0);
        OutsideCollectionReportDetailProjection detail2 = createOutsideDetailProjection(
                "OR-201", start, "", "Outside Customer A", "789 Pine St", 1, 3500.0,
                "ORD-201-2", "Labor Fee", "Overtime setup", 1000.0);

        when(orCollectionRepo.getOutsideCollectionDetailsReportByDateRange(eq(start), any(Date.class)))
                .thenReturn(List.of(detail1, detail2));

        byte[] result = service.generateOutsideCollectionReportDetails(start, end);
        assertNotNull(result);

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(result))) {
            Sheet sheet = workbook.getSheet("Outside Collection Details");
            assertNotNull(sheet);

            Row r0 = sheet.getRow(0);
            assertEquals("Report Name: Outside Collection Detailed Report", r0.getCell(0).getStringCellValue());

            // Header row (row 3)
            Row hdr = sheet.getRow(3);
            assertEquals("OR No.", hdr.getCell(0).getStringCellValue());
            assertEquals("OR Date", hdr.getCell(1).getStringCellValue());
            assertEquals("Account No", hdr.getCell(2).getStringCellValue());
            assertEquals("Customer Name", hdr.getCell(3).getStringCellValue());
            assertEquals("Address", hdr.getCell(4).getStringCellValue());
            assertEquals("Status", hdr.getCell(5).getStringCellValue());
            assertEquals("Total Amount To Pay", hdr.getCell(6).getStringCellValue());
            assertEquals("Description", hdr.getCell(7).getStringCellValue());
            assertEquals("Remarks", hdr.getCell(8).getStringCellValue());
            assertEquals("Total Amount", hdr.getCell(9).getStringCellValue());

            // Parent row (row 4)
            Row parentRow = sheet.getRow(4);
            assertEquals("OR-201", parentRow.getCell(0).getStringCellValue());
            assertEquals("", parentRow.getCell(2).getStringCellValue());
            assertEquals("Outside Customer A", parentRow.getCell(3).getStringCellValue());
            assertEquals("789 Pine St", parentRow.getCell(4).getStringCellValue());
            assertEquals("Complete", parentRow.getCell(5).getStringCellValue());
            assertEquals(3500.0, parentRow.getCell(6).getNumericCellValue());

            // Child rows (rows 5 and 6)
            Row childRow1 = sheet.getRow(5);
            assertEquals("", childRow1.getCell(0).getStringCellValue());
            assertEquals("External Water Connection", childRow1.getCell(7).getStringCellValue());
            assertEquals("Commercial site", childRow1.getCell(8).getStringCellValue());
            assertEquals(2500.0, childRow1.getCell(9).getNumericCellValue());

            Row childRow2 = sheet.getRow(6);
            assertEquals("", childRow2.getCell(0).getStringCellValue());
            assertEquals("Labor Fee", childRow2.getCell(7).getStringCellValue());
            assertEquals("Overtime setup", childRow2.getCell(8).getStringCellValue());
            assertEquals(1000.0, childRow2.getCell(9).getNumericCellValue());

            // Grand Total row (row 7)
            Row totalRow = sheet.getRow(7);
            assertEquals("GRAND TOTAL:", totalRow.getCell(5).getStringCellValue());
            assertEquals(3500.0, totalRow.getCell(6).getNumericCellValue());
            assertEquals(3500.0, totalRow.getCell(9).getNumericCellValue());
        }

        verify(orCollectionRepo).getOutsideCollectionDetailsReportByDateRange(eq(start), any(Date.class));
    }
}
