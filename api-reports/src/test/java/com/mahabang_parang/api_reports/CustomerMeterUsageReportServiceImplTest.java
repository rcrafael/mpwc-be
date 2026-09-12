package com.mahabang_parang.api_reports;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
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

import com.mahabang_parang.api_reports.model.interfaces.CustomerMeterUsageInterfaceProjection;
import com.mahabang_parang.api_reports.repository.CustomerMeterUsageRepository;
import com.mahabang_parang.api_reports.service.impl.CustomerMeterUsageReportServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerMeterUsageReportServiceImplTest {

    @Mock
    private CustomerMeterUsageRepository customerMeterUsageRepo;

    private CustomerMeterUsageReportServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CustomerMeterUsageReportServiceImpl(customerMeterUsageRepo);
    }

    private CustomerMeterUsageInterfaceProjection createMockProjection(
            String accountNo, String customerName, String meterNo,
            int jan, int feb, int mar, int apr, int may, int jun,
            int jul, int aug, int sep, int oct, int nov, int dec) {
        return new CustomerMeterUsageInterfaceProjection() {
            @Override public String getAccountNo() { return accountNo; }
            @Override public String getCustomerName() { return customerName; }
            @Override public String getMeterNo() { return meterNo; }
            @Override public Integer getJan() { return jan; }
            @Override public Integer getFeb() { return feb; }
            @Override public Integer getMar() { return mar; }
            @Override public Integer getApr() { return apr; }
            @Override public Integer getMay() { return may; }
            @Override public Integer getJun() { return jun; }
            @Override public Integer getJul() { return jul; }
            @Override public Integer getAug() { return aug; }
            @Override public Integer getSep() { return sep; }
            @Override public Integer getOct() { return oct; }
            @Override public Integer getNov() { return nov; }
            @Override public Integer getDec() { return dec; }
        };
    }

    @Test
    void testGenerateCustomerMeterUsageExcel() throws Exception {
        CustomerMeterUsageInterfaceProjection meter1 = createMockProjection(
                "ACCT-001", "Dela Cruz, Juan", "MTR-101",
                10, 12, 15, 11, 14, 13, 16, 17, 12, 14, 15, 18);
        CustomerMeterUsageInterfaceProjection meter2 = createMockProjection(
                "ACCT-001", "Dela Cruz, Juan", "MTR-102",
                5, 6, 7, 5, 8, 7, 6, 9, 8, 7, 6, 10);
        CustomerMeterUsageInterfaceProjection meter3 = createMockProjection(
                "ACCT-002", "Santos, Maria", "MTR-201",
                20, 22, 21, 23, 25, 24, 26, 27, 25, 24, 23, 28);

        when(customerMeterUsageRepo.getCustomerMeterUsagePerYear(2024))
                .thenReturn(Arrays.asList(meter1, meter2, meter3));

        byte[] excelBytes = service.generateCustomerMeterUsageExcel(2024);
        assertNotNull(excelBytes);
        assertTrue(excelBytes.length > 0);

        try (Workbook wb = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
            Sheet sheet = wb.getSheet("Meter Usage 2024");
            assertNotNull(sheet);

            // Row 4 is header row
            Row headerRow = sheet.getRow(4);
            assertNotNull(headerRow);
            assertEquals("AccountNo", headerRow.getCell(0).getStringCellValue());
            assertEquals("Customer Name", headerRow.getCell(1).getStringCellValue());
            assertEquals("Meter No", headerRow.getCell(2).getStringCellValue());
            assertEquals("Jan", headerRow.getCell(3).getStringCellValue());
            assertEquals("Dec", headerRow.getCell(14).getStringCellValue());
            assertEquals("Total", headerRow.getCell(15).getStringCellValue());

            // Row 5: meter 1
            Row r5 = sheet.getRow(5);
            assertEquals("ACCT-001", r5.getCell(0).getStringCellValue());
            assertEquals("Dela Cruz, Juan", r5.getCell(1).getStringCellValue());
            assertEquals("MTR-101", r5.getCell(2).getStringCellValue());
            assertEquals(10.0, r5.getCell(3).getNumericCellValue());
            assertEquals(167.0, r5.getCell(15).getNumericCellValue()); // sum of 10..18

            // Row 6: meter 2 (same customer, displayed next to meter 1)
            Row r6 = sheet.getRow(6);
            assertEquals("ACCT-001", r6.getCell(0).getStringCellValue());
            assertEquals("Dela Cruz, Juan", r6.getCell(1).getStringCellValue());
            assertEquals("MTR-102", r6.getCell(2).getStringCellValue());
            assertEquals(5.0, r6.getCell(3).getNumericCellValue());
            assertEquals(84.0, r6.getCell(15).getNumericCellValue());

            // Row 7: meter 3
            Row r7 = sheet.getRow(7);
            assertEquals("ACCT-002", r7.getCell(0).getStringCellValue());
            assertEquals("Santos, Maria", r7.getCell(1).getStringCellValue());
            assertEquals("MTR-201", r7.getCell(2).getStringCellValue());

            // Row 8: Grand total row
            Row r8 = sheet.getRow(8);
            assertEquals("GRAND TOTAL:", r8.getCell(2).getStringCellValue());
            assertEquals(35.0, r8.getCell(3).getNumericCellValue()); // 10 + 5 + 20 = 35
            assertEquals(539.0, r8.getCell(15).getNumericCellValue()); // 167 + 84 + 288 = 539
        }
    }
}
