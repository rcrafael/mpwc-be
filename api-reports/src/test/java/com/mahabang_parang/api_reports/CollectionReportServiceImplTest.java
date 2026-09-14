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
}
