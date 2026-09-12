package com.mahabang_parang.api_reports.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_reports.model.interfaces.CustomerMeterUsageInterfaceProjection;
import com.mahabang_parang.api_reports.repository.CustomerMeterUsageRepository;
import com.mahabang_parang.api_reports.service.CustomerMeterUsageReportService;

@Service
public class CustomerMeterUsageReportServiceImpl implements CustomerMeterUsageReportService {

    private final CustomerMeterUsageRepository customerMeterUsageRepo;

    public CustomerMeterUsageReportServiceImpl(CustomerMeterUsageRepository customerMeterUsageRepo) {
        this.customerMeterUsageRepo = customerMeterUsageRepo;
    }

    @Override
    public List<CustomerMeterUsageInterfaceProjection> getCustomerMeterUsageData(int year) {
        return customerMeterUsageRepo.getCustomerMeterUsagePerYear(year);
    }

    @Override
    public byte[] generateCustomerMeterUsageExcel(int year) {
        List<CustomerMeterUsageInterfaceProjection> data = getCustomerMeterUsageData(year);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CreationHelper ch = workbook.getCreationHelper();
            Sheet sheet = workbook.createSheet("Meter Usage " + year);

            // ── Fonts ─────────────────────────────────────────────────────────────
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);

            Font subHeaderFont = workbook.createFont();
            subHeaderFont.setBold(true);
            subHeaderFont.setFontHeightInPoints((short) 10);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);

            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 9);

            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalFont.setFontHeightInPoints((short) 10);

            // ── Cell Styles ───────────────────────────────────────────────────────
            // Title style
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);

            // Meta sub-header style
            CellStyle metaStyle = workbook.createCellStyle();
            metaStyle.setFont(subHeaderFont);

            // Table Header style
            CellStyle tableHeaderStyle = workbook.createCellStyle();
            tableHeaderStyle.setFont(headerFont);
            tableHeaderStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            tableHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            tableHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
            tableHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            tableHeaderStyle.setBorderTop(BorderStyle.MEDIUM);
            tableHeaderStyle.setBorderBottom(BorderStyle.MEDIUM);
            tableHeaderStyle.setBorderLeft(BorderStyle.THIN);
            tableHeaderStyle.setBorderRight(BorderStyle.THIN);

            // Data styles - text left
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setFont(dataFont);
            textStyle.setAlignment(HorizontalAlignment.LEFT);
            textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            // Data styles - text center (e.g. Account No, Meter No)
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setFont(dataFont);
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerStyle.setBorderTop(BorderStyle.THIN);
            centerStyle.setBorderBottom(BorderStyle.THIN);
            centerStyle.setBorderLeft(BorderStyle.THIN);
            centerStyle.setBorderRight(BorderStyle.THIN);

            // Number style for monthly m3Used
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setFont(dataFont);
            numberStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0"));
            numberStyle.setAlignment(HorizontalAlignment.RIGHT);
            numberStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            numberStyle.setBorderTop(BorderStyle.THIN);
            numberStyle.setBorderBottom(BorderStyle.THIN);
            numberStyle.setBorderLeft(BorderStyle.THIN);
            numberStyle.setBorderRight(BorderStyle.THIN);

            // Row Total number style (bold)
            CellStyle rowTotalStyle = workbook.createCellStyle();
            rowTotalStyle.setFont(totalFont);
            rowTotalStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0"));
            rowTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
            rowTotalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            rowTotalStyle.setBorderTop(BorderStyle.THIN);
            rowTotalStyle.setBorderBottom(BorderStyle.THIN);
            rowTotalStyle.setBorderLeft(BorderStyle.THIN);
            rowTotalStyle.setBorderRight(BorderStyle.THIN);

            // Grand Total label style
            CellStyle totalLabelStyle = workbook.createCellStyle();
            totalLabelStyle.setFont(totalFont);
            totalLabelStyle.setAlignment(HorizontalAlignment.RIGHT);
            totalLabelStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            totalLabelStyle.setBorderTop(BorderStyle.THIN);
            totalLabelStyle.setBorderBottom(BorderStyle.DOUBLE);
            totalLabelStyle.setBorderLeft(BorderStyle.THIN);
            totalLabelStyle.setBorderRight(BorderStyle.THIN);

            // Grand Total number style
            CellStyle grandTotalNumStyle = workbook.createCellStyle();
            grandTotalNumStyle.setFont(totalFont);
            grandTotalNumStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0"));
            grandTotalNumStyle.setAlignment(HorizontalAlignment.RIGHT);
            grandTotalNumStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            grandTotalNumStyle.setBorderTop(BorderStyle.THIN);
            grandTotalNumStyle.setBorderBottom(BorderStyle.DOUBLE);
            grandTotalNumStyle.setBorderLeft(BorderStyle.THIN);
            grandTotalNumStyle.setBorderRight(BorderStyle.THIN);

            // ── Report Title & Meta Info ──────────────────────────────────────────
            int rowIdx = 0;

            Row r0 = sheet.createRow(rowIdx++);
            Cell c0 = r0.createCell(0);
            c0.setCellValue("MAHABANG PARANG WATERWORKS COOPERATIVE");
            c0.setCellStyle(titleStyle);

            Row r1 = sheet.createRow(rowIdx++);
            Cell c1 = r1.createCell(0);
            c1.setCellValue("Customer Meter Usage Report - Year " + year);
            c1.setCellStyle(metaStyle);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            Row r2 = sheet.createRow(rowIdx++);
            Cell c2 = r2.createCell(0);
            c2.setCellValue("Generated on: " + sdf.format(new Date()));
            c2.setCellStyle(metaStyle);

            rowIdx++; // Blank spacer row

            // ── Table Header ──────────────────────────────────────────────────────
            String[] headers = {
                "AccountNo", "Customer Name", "Meter No",
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
                "Total"
            };

            Row headerRow = sheet.createRow(rowIdx++);
            headerRow.setHeightInPoints(24);
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                cell.setCellStyle(tableHeaderStyle);
            }

            // ── Data Rows ─────────────────────────────────────────────────────────
            long[] monthlyGrandTotals = new long[12];
            long overallGrandTotal = 0;

            for (CustomerMeterUsageInterfaceProjection item : data) {
                Row row = sheet.createRow(rowIdx++);
                int colIdx = 0;

                // Account No
                Cell cellAcct = row.createCell(colIdx++);
                cellAcct.setCellValue(item.getAccountNo() != null ? item.getAccountNo() : "");
                cellAcct.setCellStyle(centerStyle);

                // Customer Name
                Cell cellName = row.createCell(colIdx++);
                cellName.setCellValue(item.getCustomerName() != null ? item.getCustomerName() : "");
                cellName.setCellStyle(textStyle);

                // Meter No
                Cell cellMeter = row.createCell(colIdx++);
                cellMeter.setCellValue(item.getMeterNo() != null ? item.getMeterNo() : "");
                cellMeter.setCellStyle(centerStyle);

                // Monthly usage values Jan to Dec
                int[] months = {
                    item.getJan() != null ? item.getJan() : 0,
                    item.getFeb() != null ? item.getFeb() : 0,
                    item.getMar() != null ? item.getMar() : 0,
                    item.getApr() != null ? item.getApr() : 0,
                    item.getMay() != null ? item.getMay() : 0,
                    item.getJun() != null ? item.getJun() : 0,
                    item.getJul() != null ? item.getJul() : 0,
                    item.getAug() != null ? item.getAug() : 0,
                    item.getSep() != null ? item.getSep() : 0,
                    item.getOct() != null ? item.getOct() : 0,
                    item.getNov() != null ? item.getNov() : 0,
                    item.getDec() != null ? item.getDec() : 0
                };

                long rowTotal = 0;
                for (int m = 0; m < 12; m++) {
                    int usage = months[m];
                    Cell cellMonth = row.createCell(colIdx++);
                    cellMonth.setCellValue(usage);
                    cellMonth.setCellStyle(numberStyle);

                    rowTotal += usage;
                    monthlyGrandTotals[m] += usage;
                }

                // Row Total
                Cell cellRowTotal = row.createCell(colIdx++);
                cellRowTotal.setCellValue(rowTotal);
                cellRowTotal.setCellStyle(rowTotalStyle);

                overallGrandTotal += rowTotal;
            }

            // ── Grand Total Row ───────────────────────────────────────────────────
            Row totalRow = sheet.createRow(rowIdx++);
            totalRow.setHeightInPoints(20);

            // Empty style for first 2 cells
            for (int c = 0; c < 2; c++) {
                Cell emptyCell = totalRow.createCell(c);
                emptyCell.setCellValue("");
                emptyCell.setCellStyle(totalLabelStyle);
            }

            Cell grandTotalLabel = totalRow.createCell(2);
            grandTotalLabel.setCellValue("GRAND TOTAL:");
            grandTotalLabel.setCellStyle(totalLabelStyle);

            for (int m = 0; m < 12; m++) {
                Cell cellMonthTotal = totalRow.createCell(3 + m);
                cellMonthTotal.setCellValue(monthlyGrandTotals[m]);
                cellMonthTotal.setCellStyle(grandTotalNumStyle);
            }

            Cell cellOverallTotal = totalRow.createCell(15);
            cellOverallTotal.setCellValue(overallGrandTotal);
            cellOverallTotal.setCellStyle(grandTotalNumStyle);

            // ── Auto-size columns ─────────────────────────────────────────────────
            for (int col = 0; col < headers.length; col++) {
                sheet.autoSizeColumn(col);
                // Ensure minimum width of ~12 characters (12 * 256 units)
                if (sheet.getColumnWidth(col) < 12 * 256) {
                    sheet.setColumnWidth(col, 12 * 256);
                }
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Customer Meter Usage Excel report: " + e.getMessage(), e);
        }
    }

}
