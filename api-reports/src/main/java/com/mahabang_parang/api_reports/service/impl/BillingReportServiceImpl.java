package com.mahabang_parang.api_reports.service.impl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_reports.model.interfaces.BillingReportDetailedProjection;
import com.mahabang_parang.api_reports.model.interfaces.BillingReportSummaryProjection;
import com.mahabang_parang.api_reports.repository.BillingReportRepository;
import com.mahabang_parang.api_reports.service.BillingReportService;
import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.service.BranchService;
import com.mahabang_parang.api_user.service.PhaseService;

@Service
public class BillingReportServiceImpl implements BillingReportService {

    private final BillingReportRepository billingReportRepo;
    private final BranchService branchService;
    private final PhaseService phaseService;

    public BillingReportServiceImpl(BillingReportRepository billingReportRepo,
                                   BranchService branchService,
                                   PhaseService phaseService) {
        this.billingReportRepo = billingReportRepo;
        this.branchService = branchService;
        this.phaseService = phaseService;
    }

    private Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private String formatMonth(Integer monthOf, Date fallbackDate) {
        if (monthOf != null && monthOf >= 1 && monthOf <= 12) {
            return Month.of(monthOf).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        }
        if (fallbackDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
            return sdf.format(fallbackDate);
        }
        return "";
    }

    @Override
    public byte[] generateBillingReportSummary(Date startDate, Date endDate, Integer branchCode, Integer phase) {
        int bCode = (branchCode == null) ? -1 : branchCode;
        int pCode = (phase == null) ? -1 : phase;
        Date adjustedEndDate = getEndOfDay(endDate);

        List<BillingReportSummaryProjection> data = billingReportRepo.getBillingReportSummary(
                startDate, adjustedEndDate, bCode, pCode);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Billing Report Summary");
            CreationHelper createHelper = workbook.getCreationHelper();

            // Fonts
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);

            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 10);

            // Styles
            CellStyle mainHeaderStyle = workbook.createCellStyle();
            mainHeaderStyle.setFont(headerFont);

            CellStyle tableHeaderStyle = workbook.createCellStyle();
            tableHeaderStyle.setFont(headerFont);
            tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
            tableHeaderStyle.setBorderTop(BorderStyle.THIN);
            tableHeaderStyle.setBorderRight(BorderStyle.THIN);
            tableHeaderStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setFont(dataFont);
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMM dd, yyyy"));
            dateStyle.setBorderBottom(BorderStyle.THIN);
            dateStyle.setBorderTop(BorderStyle.THIN);
            dateStyle.setBorderRight(BorderStyle.THIN);
            dateStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setFont(dataFont);
            numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            numberStyle.setBorderBottom(BorderStyle.THIN);
            numberStyle.setBorderTop(BorderStyle.THIN);
            numberStyle.setBorderRight(BorderStyle.THIN);
            numberStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle intStyle = workbook.createCellStyle();
            intStyle.setFont(dataFont);
            intStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
            intStyle.setAlignment(HorizontalAlignment.RIGHT);
            intStyle.setBorderBottom(BorderStyle.THIN);
            intStyle.setBorderTop(BorderStyle.THIN);
            intStyle.setBorderRight(BorderStyle.THIN);
            intStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle numberTotalStyle = workbook.createCellStyle();
            numberTotalStyle.setFont(headerFont);
            numberTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            numberTotalStyle.setBorderBottom(BorderStyle.THIN);
            numberTotalStyle.setBorderTop(BorderStyle.THIN);
            numberTotalStyle.setBorderRight(BorderStyle.THIN);
            numberTotalStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle intTotalStyle = workbook.createCellStyle();
            intTotalStyle.setFont(headerFont);
            intTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
            intTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
            intTotalStyle.setBorderBottom(BorderStyle.THIN);
            intTotalStyle.setBorderTop(BorderStyle.THIN);
            intTotalStyle.setBorderRight(BorderStyle.THIN);
            intTotalStyle.setBorderLeft(BorderStyle.THIN);

            int rowIdx = 0;

            // Report Name
            Row reportNameRow = sheet.createRow(rowIdx++);
            Cell reportNameCell = reportNameRow.createCell(0);
            reportNameCell.setCellValue("Report Name: Billing Report Summary");
            reportNameCell.setCellStyle(mainHeaderStyle);

            // Date Range
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Row dateRangeRow = sheet.createRow(rowIdx++);
            Cell dateRangeCell = dateRangeRow.createCell(0);
            dateRangeCell.setCellValue("Report Date: " + sdf.format(startDate) + " to " + sdf.format(endDate));
            dateRangeCell.setCellStyle(mainHeaderStyle);

            // Branch & Phase lookup
            Map<Integer, String> branchMap = branchService.findAll().stream()
                    .filter(b -> b.getId() != null && b.getBranchCode() != null)
                    .collect(Collectors.toMap(BranchDto::getId, BranchDto::getBranchCode, (v1, v2) -> v1));
            Map<Integer, String> phaseMap = phaseService.findAll().stream()
                    .filter(p -> p.getId() != null && p.getPhase() != null)
                    .collect(Collectors.toMap(PhaseDto::getId, PhaseDto::getPhase, (v1, v2) -> v1));

            String branchLabel = (bCode == -1) ? "All" : branchMap.getOrDefault(bCode, String.valueOf(bCode));
            String phaseLabel = (pCode == -1) ? "All" : phaseMap.getOrDefault(pCode, String.valueOf(pCode));
            Row filterRow = sheet.createRow(rowIdx++);
            Cell filterCell = filterRow.createCell(0);
            filterCell.setCellValue("Branch Code: " + branchLabel + "   |   Phase: " + phaseLabel);
            filterCell.setCellStyle(mainHeaderStyle);

            // Blank row
            rowIdx++;

            // Table Headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] columns = {
                    "Reading No", "Account No", "Meter No", "Customer Name", "Alias",
                    "Branch Code", "Phase", "Address", "Month", "Billing Period",
                    "Due Date", "M3 Used", "Amount"
            };
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(tableHeaderStyle);
            }

            // Running totals
            long grandTotalM3Used = 0L;
            double grandTotalAmount = 0.0;

            // Data rows
            for (BillingReportSummaryProjection record : data) {
                Row row = sheet.createRow(rowIdx++);

                // Reading No
                Cell cellReadingNo = row.createCell(0);
                cellReadingNo.setCellValue(record.getReadingNo() != null ? record.getReadingNo() : "");
                cellReadingNo.setCellStyle(dataStyle);

                // Account No
                Cell cellAccountNo = row.createCell(1);
                cellAccountNo.setCellValue(record.getAccountNo() != null ? record.getAccountNo() : "");
                cellAccountNo.setCellStyle(dataStyle);

                // Meter No
                Cell cellMeterNo = row.createCell(2);
                cellMeterNo.setCellValue(record.getMeterNo() != null ? record.getMeterNo() : "");
                cellMeterNo.setCellStyle(dataStyle);

                // Customer Name
                Cell cellCustomerName = row.createCell(3);
                cellCustomerName.setCellValue(record.getCustomerName() != null ? record.getCustomerName() : "");
                cellCustomerName.setCellStyle(dataStyle);

                // Alias
                Cell cellAlias = row.createCell(4);
                cellAlias.setCellValue(record.getAlias() != null ? record.getAlias() : "");
                cellAlias.setCellStyle(dataStyle);

                // Branch Code (from CustomerMeterEntity mapped to BranchEntity string)
                Cell cellBranch = row.createCell(5);
                String branchVal = record.getBranchCode() != null
                        ? branchMap.getOrDefault(record.getBranchCode(), String.valueOf(record.getBranchCode()))
                        : "";
                cellBranch.setCellValue(branchVal);
                cellBranch.setCellStyle(dataStyle);

                // Phase (from CustomerMeterEntity mapped to PhaseEntity string)
                Cell cellPhase = row.createCell(6);
                String phaseVal = record.getPhase() != null
                        ? phaseMap.getOrDefault(record.getPhase(), String.valueOf(record.getPhase()))
                        : "";
                cellPhase.setCellValue(phaseVal);
                cellPhase.setCellStyle(dataStyle);

                // Address
                Cell cellAddress = row.createCell(7);
                cellAddress.setCellValue(record.getAddress() != null ? record.getAddress() : "");
                cellAddress.setCellStyle(dataStyle);

                // Month (MMM format)
                Cell cellMonth = row.createCell(8);
                cellMonth.setCellValue(formatMonth(record.getMonthOf(), record.getDateFrom()));
                cellMonth.setCellStyle(dataStyle);

                // Billing Period (date_from and date_to concatenated with ' - ' in MMM dd, yyyy)
                Cell cellBillingPeriod = row.createCell(9);
                String dateFromStr = record.getDateFrom() != null ? sdf.format(record.getDateFrom()) : "";
                String dateToStr = record.getDateTo() != null ? sdf.format(record.getDateTo()) : "";
                String period = (!dateFromStr.isEmpty() || !dateToStr.isEmpty()) ? (dateFromStr + " - " + dateToStr) : "";
                cellBillingPeriod.setCellValue(period);
                cellBillingPeriod.setCellStyle(dataStyle);

                // Due Date (MMM dd, yyyy)
                Cell cellDueDate = row.createCell(10);
                if (record.getDueDate() != null) {
                    cellDueDate.setCellValue(record.getDueDate());
                }
                cellDueDate.setCellStyle(dateStyle);

                // M3 Used
                Cell cellM3Used = row.createCell(11);
                if (record.getM3Used() != null) {
                    cellM3Used.setCellValue(record.getM3Used());
                    grandTotalM3Used += record.getM3Used();
                }
                cellM3Used.setCellStyle(intStyle);

                // Amount
                Cell cellAmount = row.createCell(12);
                if (record.getAmount() != null) {
                    cellAmount.setCellValue(record.getAmount());
                    grandTotalAmount += record.getAmount();
                }
                cellAmount.setCellStyle(numberStyle);
            }

            // Totals row
            Row totalRow = sheet.createRow(rowIdx++);
            for (int col = 0; col <= 9; col++) {
                totalRow.createCell(col).setCellStyle(tableHeaderStyle);
            }
            Cell totalLabelCell = totalRow.createCell(10);
            totalLabelCell.setCellValue("TOTAL:");
            totalLabelCell.setCellStyle(tableHeaderStyle);

            Cell grandTotalM3Cell = totalRow.createCell(11);
            grandTotalM3Cell.setCellValue(grandTotalM3Used);
            grandTotalM3Cell.setCellStyle(intTotalStyle);

            Cell grandTotalAmountCell = totalRow.createCell(12);
            grandTotalAmountCell.setCellValue(grandTotalAmount);
            grandTotalAmountCell.setCellStyle(numberTotalStyle);

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Billing Report Summary Excel: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] generateBillingReportDetailed(Date startDate, Date endDate, Integer branchCode, Integer phase) {
        int bCode = (branchCode == null) ? -1 : branchCode;
        int pCode = (phase == null) ? -1 : phase;
        Date adjustedEndDate = getEndOfDay(endDate);

        List<BillingReportDetailedProjection> data = billingReportRepo.getBillingReportDetailed(
                startDate, adjustedEndDate, bCode, pCode);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Billing Report Detailed");
            CreationHelper createHelper = workbook.getCreationHelper();

            // Fonts
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);

            Font dataFont = workbook.createFont();
            dataFont.setFontHeightInPoints((short) 10);

            // Styles
            CellStyle mainHeaderStyle = workbook.createCellStyle();
            mainHeaderStyle.setFont(headerFont);

            CellStyle tableHeaderStyle = workbook.createCellStyle();
            tableHeaderStyle.setFont(headerFont);
            tableHeaderStyle.setBorderBottom(BorderStyle.THIN);
            tableHeaderStyle.setBorderTop(BorderStyle.THIN);
            tableHeaderStyle.setBorderRight(BorderStyle.THIN);
            tableHeaderStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(dataFont);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setFont(dataFont);
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMM dd, yyyy"));
            dateStyle.setBorderBottom(BorderStyle.THIN);
            dateStyle.setBorderTop(BorderStyle.THIN);
            dateStyle.setBorderRight(BorderStyle.THIN);
            dateStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setFont(dataFont);
            numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            numberStyle.setBorderBottom(BorderStyle.THIN);
            numberStyle.setBorderTop(BorderStyle.THIN);
            numberStyle.setBorderRight(BorderStyle.THIN);
            numberStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle intStyle = workbook.createCellStyle();
            intStyle.setFont(dataFont);
            intStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
            intStyle.setAlignment(HorizontalAlignment.RIGHT);
            intStyle.setBorderBottom(BorderStyle.THIN);
            intStyle.setBorderTop(BorderStyle.THIN);
            intStyle.setBorderRight(BorderStyle.THIN);
            intStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle numberTotalStyle = workbook.createCellStyle();
            numberTotalStyle.setFont(headerFont);
            numberTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            numberTotalStyle.setBorderBottom(BorderStyle.THIN);
            numberTotalStyle.setBorderTop(BorderStyle.THIN);
            numberTotalStyle.setBorderRight(BorderStyle.THIN);
            numberTotalStyle.setBorderLeft(BorderStyle.THIN);

            CellStyle intTotalStyle = workbook.createCellStyle();
            intTotalStyle.setFont(headerFont);
            intTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0"));
            intTotalStyle.setAlignment(HorizontalAlignment.RIGHT);
            intTotalStyle.setBorderBottom(BorderStyle.THIN);
            intTotalStyle.setBorderTop(BorderStyle.THIN);
            intTotalStyle.setBorderRight(BorderStyle.THIN);
            intTotalStyle.setBorderLeft(BorderStyle.THIN);

            int rowIdx = 0;

            // Report Name
            Row reportNameRow = sheet.createRow(rowIdx++);
            Cell reportNameCell = reportNameRow.createCell(0);
            reportNameCell.setCellValue("Report Name: Billing Report Detailed");
            reportNameCell.setCellStyle(mainHeaderStyle);

            // Date Range
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
            Row dateRangeRow = sheet.createRow(rowIdx++);
            Cell dateRangeCell = dateRangeRow.createCell(0);
            dateRangeCell.setCellValue("Report Date: " + sdf.format(startDate) + " to " + sdf.format(endDate));
            dateRangeCell.setCellStyle(mainHeaderStyle);

            // Branch & Phase lookup
            Map<Integer, String> branchMap = branchService.findAll().stream()
                    .filter(b -> b.getId() != null && b.getBranchCode() != null)
                    .collect(Collectors.toMap(BranchDto::getId, BranchDto::getBranchCode, (v1, v2) -> v1));
            Map<Integer, String> phaseMap = phaseService.findAll().stream()
                    .filter(p -> p.getId() != null && p.getPhase() != null)
                    .collect(Collectors.toMap(PhaseDto::getId, PhaseDto::getPhase, (v1, v2) -> v1));

            String branchLabel = (bCode == -1) ? "All" : branchMap.getOrDefault(bCode, String.valueOf(bCode));
            String phaseLabel = (pCode == -1) ? "All" : phaseMap.getOrDefault(pCode, String.valueOf(pCode));
            Row filterRow = sheet.createRow(rowIdx++);
            Cell filterCell = filterRow.createCell(0);
            filterCell.setCellValue("Branch Code: " + branchLabel + "   |   Phase: " + phaseLabel);
            filterCell.setCellStyle(mainHeaderStyle);

            // Blank row
            rowIdx++;

            // Table Headers
            Row headerRow = sheet.createRow(rowIdx++);
            String[] columns = {
                    "Reading No", "Account No", "Meter No", "Customer Name", "Alias",
                    "Branch Code", "Phase", "Address", "Month", "Billing Period",
                    "Due Date", "Previous M3", "Present M3", "M3 Used",
                    "Reading Amount", "Parts", "Maintenance Fee", "Septage Fee", "Less", "Total Amount"
            };
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
                cell.setCellStyle(tableHeaderStyle);
            }

            // Running totals
            long grandTotalM3Used = 0L;
            double grandTotalReadingAmount = 0.0;
            double grandTotalParts = 0.0;
            double grandTotalMaintenanceFee = 0.0;
            double grandTotalSeptageFee = 0.0;
            double grandTotalLessAmount = 0.0;
            double grandTotalTotalAmount = 0.0;

            // Data rows
            for (BillingReportDetailedProjection record : data) {
                Row row = sheet.createRow(rowIdx++);

                // Reading No
                Cell cellReadingNo = row.createCell(0);
                cellReadingNo.setCellValue(record.getReadingNo() != null ? record.getReadingNo() : "");
                cellReadingNo.setCellStyle(dataStyle);

                // Account No
                Cell cellAccountNo = row.createCell(1);
                cellAccountNo.setCellValue(record.getAccountNo() != null ? record.getAccountNo() : "");
                cellAccountNo.setCellStyle(dataStyle);

                // Meter No
                Cell cellMeterNo = row.createCell(2);
                cellMeterNo.setCellValue(record.getMeterNo() != null ? record.getMeterNo() : "");
                cellMeterNo.setCellStyle(dataStyle);

                // Customer Name
                Cell cellCustomerName = row.createCell(3);
                cellCustomerName.setCellValue(record.getCustomerName() != null ? record.getCustomerName() : "");
                cellCustomerName.setCellStyle(dataStyle);

                // Alias
                Cell cellAlias = row.createCell(4);
                cellAlias.setCellValue(record.getAlias() != null ? record.getAlias() : "");
                cellAlias.setCellStyle(dataStyle);

                // Branch Code
                Cell cellBranch = row.createCell(5);
                String branchVal = record.getBranchCode() != null
                        ? branchMap.getOrDefault(record.getBranchCode(), String.valueOf(record.getBranchCode()))
                        : "";
                cellBranch.setCellValue(branchVal);
                cellBranch.setCellStyle(dataStyle);

                // Phase
                Cell cellPhase = row.createCell(6);
                String phaseVal = record.getPhase() != null
                        ? phaseMap.getOrDefault(record.getPhase(), String.valueOf(record.getPhase()))
                        : "";
                cellPhase.setCellValue(phaseVal);
                cellPhase.setCellStyle(dataStyle);

                // Address
                Cell cellAddress = row.createCell(7);
                cellAddress.setCellValue(record.getAddress() != null ? record.getAddress() : "");
                cellAddress.setCellStyle(dataStyle);

                // Month (MMM format)
                Cell cellMonth = row.createCell(8);
                cellMonth.setCellValue(formatMonth(record.getMonthOf(), record.getDateFrom()));
                cellMonth.setCellStyle(dataStyle);

                // Billing Period
                Cell cellBillingPeriod = row.createCell(9);
                String dateFromStr = record.getDateFrom() != null ? sdf.format(record.getDateFrom()) : "";
                String dateToStr = record.getDateTo() != null ? sdf.format(record.getDateTo()) : "";
                String period = (!dateFromStr.isEmpty() || !dateToStr.isEmpty()) ? (dateFromStr + " - " + dateToStr) : "";
                cellBillingPeriod.setCellValue(period);
                cellBillingPeriod.setCellStyle(dataStyle);

                // Due Date
                Cell cellDueDate = row.createCell(10);
                if (record.getDueDate() != null) {
                    cellDueDate.setCellValue(record.getDueDate());
                }
                cellDueDate.setCellStyle(dateStyle);

                // Previous M3
                Cell cellPrevious = row.createCell(11);
                if (record.getPrevious() != null) {
                    cellPrevious.setCellValue(record.getPrevious());
                }
                cellPrevious.setCellStyle(intStyle);

                // Present M3
                Cell cellPresent = row.createCell(12);
                if (record.getPresent() != null) {
                    cellPresent.setCellValue(record.getPresent());
                }
                cellPresent.setCellStyle(intStyle);

                // M3 Used
                Cell cellM3Used = row.createCell(13);
                if (record.getM3Used() != null) {
                    cellM3Used.setCellValue(record.getM3Used());
                    grandTotalM3Used += record.getM3Used();
                }
                cellM3Used.setCellStyle(intStyle);

                // Reading Amount
                Cell cellReadingAmount = row.createCell(14);
                if (record.getReadingAmount() != null) {
                    cellReadingAmount.setCellValue(record.getReadingAmount());
                    grandTotalReadingAmount += record.getReadingAmount();
                }
                cellReadingAmount.setCellStyle(numberStyle);

                // Parts
                Cell cellParts = row.createCell(15);
                if (record.getParts() != null) {
                    cellParts.setCellValue(record.getParts());
                    grandTotalParts += record.getParts();
                }
                cellParts.setCellStyle(numberStyle);

                // Maintenance Fee
                Cell cellMaint = row.createCell(16);
                if (record.getMaintenanceFee() != null) {
                    cellMaint.setCellValue(record.getMaintenanceFee());
                    grandTotalMaintenanceFee += record.getMaintenanceFee();
                }
                cellMaint.setCellStyle(numberStyle);

                // Septage Fee
                Cell cellSeptage = row.createCell(17);
                if (record.getSeptageFee() != null) {
                    cellSeptage.setCellValue(record.getSeptageFee());
                    grandTotalSeptageFee += record.getSeptageFee();
                }
                cellSeptage.setCellStyle(numberStyle);

                // Less
                Cell cellLess = row.createCell(18);
                if (record.getLessAmount() != null) {
                    cellLess.setCellValue(record.getLessAmount());
                    grandTotalLessAmount += record.getLessAmount();
                }
                cellLess.setCellStyle(numberStyle);

                // Total Amount
                Cell cellTotalAmount = row.createCell(19);
                if (record.getTotalAmount() != null) {
                    cellTotalAmount.setCellValue(record.getTotalAmount());
                    grandTotalTotalAmount += record.getTotalAmount();
                }
                cellTotalAmount.setCellStyle(numberStyle);
            }

            // Totals row
            Row totalRow = sheet.createRow(rowIdx++);
            for (int col = 0; col <= 12; col++) {
                totalRow.createCell(col).setCellStyle(tableHeaderStyle);
            }
            Cell totalLabelCell = totalRow.createCell(10);
            totalLabelCell.setCellValue("TOTAL:");
            totalLabelCell.setCellStyle(tableHeaderStyle);

            Cell grandTotalM3Cell = totalRow.createCell(13);
            grandTotalM3Cell.setCellValue(grandTotalM3Used);
            grandTotalM3Cell.setCellStyle(intTotalStyle);

            Cell grandTotalReadingAmountCell = totalRow.createCell(14);
            grandTotalReadingAmountCell.setCellValue(grandTotalReadingAmount);
            grandTotalReadingAmountCell.setCellStyle(numberTotalStyle);

            Cell grandTotalPartsCell = totalRow.createCell(15);
            grandTotalPartsCell.setCellValue(grandTotalParts);
            grandTotalPartsCell.setCellStyle(numberTotalStyle);

            Cell grandTotalMaintCell = totalRow.createCell(16);
            grandTotalMaintCell.setCellValue(grandTotalMaintenanceFee);
            grandTotalMaintCell.setCellStyle(numberTotalStyle);

            Cell grandTotalSeptageCell = totalRow.createCell(17);
            grandTotalSeptageCell.setCellValue(grandTotalSeptageFee);
            grandTotalSeptageCell.setCellStyle(numberTotalStyle);

            Cell grandTotalLessCell = totalRow.createCell(18);
            grandTotalLessCell.setCellValue(grandTotalLessAmount);
            grandTotalLessCell.setCellStyle(numberTotalStyle);

            Cell grandTotalTotalAmountCell = totalRow.createCell(19);
            grandTotalTotalAmountCell.setCellValue(grandTotalTotalAmount);
            grandTotalTotalAmountCell.setCellStyle(numberTotalStyle);

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate Billing Report Detailed Excel: " + e.getMessage(), e);
        }
    }
}
