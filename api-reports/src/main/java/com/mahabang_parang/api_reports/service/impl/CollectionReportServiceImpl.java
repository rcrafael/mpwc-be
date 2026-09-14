package com.mahabang_parang.api_reports.service.impl;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_billing.repository.BillingRepository;
import com.mahabang_parang.api_payment.repository.OfficialReceiptHeaderRepository;
import com.mahabang_parang.api_reports.model.PaginationResponse;
import com.mahabang_parang.api_reports.model.dto.DashboardCardDataDto;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionInterfaceProjection;
import com.mahabang_parang.api_reports.model.interfaces.ShareCapCbuSavingsCollectionListInterfaceProjection;
import com.mahabang_parang.api_reports.repository.ORCollectionRepository;
import com.mahabang_parang.api_reports.service.CollectionReportService;
import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.repository.CustomerRepository;
import com.mahabang_parang.api_user.service.BranchService;
import com.mahabang_parang.api_user.service.PhaseService;

@Service
public class CollectionReportServiceImpl implements CollectionReportService {

     private final ORCollectionRepository orCollectionRepo;
     private final BillingRepository billingRepo;
     private final CustomerRepository customerRepo;
     private final CustomerMeterRepository customerMeterRepo;
     private final OfficialReceiptHeaderRepository orHeaderRepo;
     private final BranchService branchService;
     private final PhaseService phaseService;

     public CollectionReportServiceImpl(ORCollectionRepository _orCollectionRepo,
                                        BillingRepository _billingRepo,
                                        CustomerRepository _customerRepo,
                                        CustomerMeterRepository _customerMeterRepo,
                                        OfficialReceiptHeaderRepository _orHeaderRepo,
                                        BranchService _branchService,
                                        PhaseService _phaseService
     ) {
          this.orCollectionRepo = _orCollectionRepo;
          this.billingRepo = _billingRepo;
          this.customerRepo = _customerRepo;
          this.customerMeterRepo = _customerMeterRepo;
          this.orHeaderRepo = _orHeaderRepo;
          this.branchService = _branchService;
          this.phaseService = _phaseService;
     }

     private Date getEndOfDay(Date date) {
          if (date == null) {
               return null;
          }
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(date);
          cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
          cal.set(java.util.Calendar.MINUTE, 59);
          cal.set(java.util.Calendar.SECOND, 59);
          cal.set(java.util.Calendar.MILLISECOND, 999);
          return cal.getTime();
     }

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerId(String customerId) {
          ShareCapCbuSavingsCollectionInterfaceProjection result = orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerId(customerId);
          return result;
     }

     @Override
     public ShareCapCbuSavingsCollectionInterfaceProjection getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate) {
          Date adjustedEndDate = getEndOfDay(endDate);
          ShareCapCbuSavingsCollectionInterfaceProjection result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(customerId, startDate, adjustedEndDate);
          return result;
     }

     @Override
     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            String customerId, 
                                                            Date startDate, 
                                                            Date endDate) {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());
          Date adjustedEndDate = getEndOfDay(endDate);

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(customerId, startDate, adjustedEndDate, pageable);
          return new PaginationResponse<>(
               result.getContent(),
               result.getNumber(),
               result.getSize(),
               result.getTotalPages(),
               result.getTotalElements()
          );

     }

     @Override
     public PaginationResponse<ShareCapCbuSavingsCollectionListInterfaceProjection> getShareCapCbuSavingsCollectionListByDateRange(
                                                            int pageNo,
                                                            int pageSize,
                                                            Date startDate, 
                                                            Date endDate) {

          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("orDate").descending());
          Date adjustedEndDate = getEndOfDay(endDate);

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByDateRange(startDate, adjustedEndDate, pageable);
          return new PaginationResponse<>(
               result.getContent(),
               result.getNumber(),
               result.getSize(),
               result.getTotalPages(),
               result.getTotalElements()
          );

     }

     
     @Override
     public DashboardCardDataDto getDashboardCardData(Date startDate, Date endDate) {
          
          Date adjustedEndDate = getEndOfDay(endDate);
          DashboardCardDataDto result = new DashboardCardDataDto();
          Long userCount = customerRepo.count();
          Long activeMeterCount = customerMeterRepo.countByStatus(6); //6 = Active
          Integer totalCubicMeterUsage =  billingRepo.totalCubicMeterUsageByDate(startDate, adjustedEndDate);
          Double totalUnpaidBillingAmount = billingRepo.sumOfUnpaidBillingByDate(startDate, adjustedEndDate);
          Double totalOfficialReceiptAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, adjustedEndDate, false);
          Double totalOtherCollectionAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, adjustedEndDate, true);
          double totalShareCapAmount = orCollectionRepo.sumOfShareCapitalByDateRange(startDate, adjustedEndDate);
          double totalCbuAmount = orCollectionRepo.sumOfCbuByDateRange(startDate, adjustedEndDate);

          result.setUserCount(userCount);
          result.setActiveMeterCount(activeMeterCount);
          result.setTotalCubicMeterUsage(totalCubicMeterUsage);
          result.setTotalUnpaidBillingAmount(totalUnpaidBillingAmount);
          result.setTotalOfficialReceiptAmount(totalOfficialReceiptAmount);
          result.setTotalOtherCollectionAmount(totalOtherCollectionAmount);
          result.setTotalShareCapAmount(totalShareCapAmount);
          result.setTotalCbuAmount(totalCbuAmount);
          
          return result;

     }


     @Override
     public byte[] generateCollectionReportExcel(Date startDate, Date endDate, Integer branchCode, Integer phase) {
          int bCode = (branchCode == null) ? -1 : branchCode;
          int pCode = (phase == null) ? -1 : phase;
          Date adjustedEndDate = getEndOfDay(endDate);

          java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportInterfaceProjection> data = 
                    orCollectionRepo.getCollectionReportByDateRange(startDate, adjustedEndDate, bCode, pCode);
          
          try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
               java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
               
               org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Collection Report");
               
               // Font for headers
               org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
               headerFont.setBold(true);
               headerFont.setFontHeightInPoints((short) 12);
               
               // Main Header Style
               org.apache.poi.ss.usermodel.CellStyle mainHeaderStyle = workbook.createCellStyle();
               mainHeaderStyle.setFont(headerFont);
               
               // Table Header Style
               org.apache.poi.ss.usermodel.CellStyle tableHeaderStyle = workbook.createCellStyle();
               tableHeaderStyle.setFont(headerFont);
               tableHeaderStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               tableHeaderStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               tableHeaderStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               tableHeaderStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               
               // Data Row Style
               org.apache.poi.ss.usermodel.CellStyle dataStyle = workbook.createCellStyle();
               dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               
               // Date Style
               org.apache.poi.ss.usermodel.CellStyle dateStyle = workbook.createCellStyle();
               org.apache.poi.ss.usermodel.CreationHelper createHelper = workbook.getCreationHelper();
               dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("MMM dd, yyyy"));
               dateStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dateStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dateStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               dateStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               
               // Number Style (comma-separated, 2 decimal places)
               org.apache.poi.ss.usermodel.CellStyle numberStyle = workbook.createCellStyle();
               numberStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
               numberStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Number Style for Totals row (bold + comma-separated, 2 decimal places)
               org.apache.poi.ss.usermodel.CellStyle numberTotalStyle = workbook.createCellStyle();
               numberTotalStyle.setFont(headerFont);
               numberTotalStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
               numberTotalStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberTotalStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberTotalStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               numberTotalStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               
               int rowIdx = 0;
               
               // Report Name
               org.apache.poi.ss.usermodel.Row reportNameRow = sheet.createRow(rowIdx++);
               org.apache.poi.ss.usermodel.Cell reportNameCell = reportNameRow.createCell(0);
               reportNameCell.setCellValue("Report Name: Collection Report");
               reportNameCell.setCellStyle(mainHeaderStyle);
               
               // Date Range
               java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
               org.apache.poi.ss.usermodel.Row dateRangeRow = sheet.createRow(rowIdx++);
               org.apache.poi.ss.usermodel.Cell dateRangeCell = dateRangeRow.createCell(0);
               dateRangeCell.setCellValue("Report Date: " + sdf.format(startDate) + " to " + sdf.format(endDate));
               dateRangeCell.setCellStyle(mainHeaderStyle);
               
                // Branch Code and Phase
                Map<Integer, String> branchMap = branchService.findAll().stream()
                     .filter(b -> b.getId() != null && b.getBranchCode() != null)
                     .collect(Collectors.toMap(BranchDto::getId, BranchDto::getBranchCode, (v1, v2) -> v1));
                Map<Integer, String> phaseMap = phaseService.findAll().stream()
                     .filter(p -> p.getId() != null && p.getPhase() != null)
                     .collect(Collectors.toMap(PhaseDto::getId, PhaseDto::getPhase, (v1, v2) -> v1));

                String branchLabel = (bCode == -1) ? "All" : branchMap.getOrDefault(bCode, String.valueOf(bCode));
                String phaseLabel  = (pCode == -1) ? "All" : phaseMap.getOrDefault(pCode, String.valueOf(pCode));
                org.apache.poi.ss.usermodel.Row filterRow = sheet.createRow(rowIdx++);
                org.apache.poi.ss.usermodel.Cell filterCell = filterRow.createCell(0);
                filterCell.setCellValue("Branch Code: " + branchLabel + "   |   Phase: " + phaseLabel);
                filterCell.setCellStyle(mainHeaderStyle);

               // Empty row before table
               rowIdx++;
               
               // Table Headers
               org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(rowIdx++);
               String[] columns = {"OR No", "OR Date", "Account No", "Alias", "First Name", "Middle Name", "Last Name","Total Billing Amount","Total Share Amount", "Senior Discount", "Total Amount Pay"};
               for (int col = 0; col < columns.length; col++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(col);
                    cell.setCellValue(columns[col]);
                    cell.setCellStyle(tableHeaderStyle);
               }
               
               // Running totals
               double grandTotalAmountPay = 0.0;
               double grandTotalShareAmount = 0.0;
               double grandTotalBillingAmount = 0.0;
               double grandSeniorDiscount = 0.0;

               // Data Rows
               for (com.mahabang_parang.api_reports.model.interfaces.CollectionReportInterfaceProjection record : data) {
                    org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);

                    org.apache.poi.ss.usermodel.Cell cellOrNo = row.createCell(0);
                    cellOrNo.setCellValue(record.getOrNo() != null ? record.getOrNo() : "");
                    cellOrNo.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellDate = row.createCell(1);
                    if (record.getOrDate() != null) {
                         cellDate.setCellValue(record.getOrDate());
                    }
                    cellDate.setCellStyle(dateStyle);

                    org.apache.poi.ss.usermodel.Cell cellAccountNo = row.createCell(2);
                    cellAccountNo.setCellValue(record.getAccountNo() != null ? record.getAccountNo() : "");
                    cellAccountNo.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellAlias = row.createCell(3);
                    cellAlias.setCellValue(record.getCustomerAlias() != null ? record.getCustomerAlias() : "");
                    cellAlias.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellFirstName = row.createCell(4);
                    cellFirstName.setCellValue(record.getFirstName() != null ? record.getFirstName() : "");
                    cellFirstName.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellMiddleName = row.createCell(5);
                    cellMiddleName.setCellValue(record.getMiddleName() != null ? record.getMiddleName() : "");
                    cellMiddleName.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellLastName = row.createCell(6);
                    cellLastName.setCellValue(record.getLastName() != null ? record.getLastName() : "");
                    cellLastName.setCellStyle(dataStyle);

                    org.apache.poi.ss.usermodel.Cell cellTotalBilling = row.createCell(7);
                    if (record.getTotalBillingAmount() != null) {
                         cellTotalBilling.setCellValue(record.getTotalBillingAmount());
                         grandTotalBillingAmount += record.getTotalBillingAmount();
                    }
                    cellTotalBilling.setCellStyle(numberStyle);

                    org.apache.poi.ss.usermodel.Cell cellTotalShare = row.createCell(8);
                    if (record.getTotalShareAmount() != null) {
                         cellTotalShare.setCellValue(record.getTotalShareAmount());
                         grandTotalShareAmount += record.getTotalShareAmount();
                    }
                    cellTotalShare.setCellStyle(numberStyle);

                    org.apache.poi.ss.usermodel.Cell cellSeniorDiscount = row.createCell(9);
                    if (record.getSeniorDiscount() != null) {
                         cellSeniorDiscount.setCellValue(record.getSeniorDiscount());
                         grandSeniorDiscount += record.getSeniorDiscount();
                    }
                    cellSeniorDiscount.setCellStyle(numberStyle);

                    org.apache.poi.ss.usermodel.Cell cellTotalAmount = row.createCell(10);
                    if (record.getTotalAmountPay() != null) {
                         cellTotalAmount.setCellValue(record.getTotalAmountPay());
                         grandTotalAmountPay += record.getTotalAmountPay();
                    }
                    cellTotalAmount.setCellStyle(numberStyle);

               }

               // Totals Row
               org.apache.poi.ss.usermodel.Row totalRow = sheet.createRow(rowIdx++);
               for (int col = 0; col <= 5; col++) {
                    totalRow.createCell(col).setCellStyle(tableHeaderStyle);
               }
               org.apache.poi.ss.usermodel.Cell cellTotalLabel = totalRow.createCell(6);
               cellTotalLabel.setCellValue("TOTAL:");
               cellTotalLabel.setCellStyle(tableHeaderStyle);

               org.apache.poi.ss.usermodel.Cell cellGrandTotalBilling = totalRow.createCell(7);
               cellGrandTotalBilling.setCellValue(grandTotalBillingAmount);
               cellGrandTotalBilling.setCellStyle(numberTotalStyle);

               org.apache.poi.ss.usermodel.Cell cellGrandTotalShare = totalRow.createCell(8);
               cellGrandTotalShare.setCellValue(grandTotalShareAmount);
               cellGrandTotalShare.setCellStyle(numberTotalStyle);

               org.apache.poi.ss.usermodel.Cell cellGrandSeniorDiscount = totalRow.createCell(9);
               cellGrandSeniorDiscount.setCellValue(grandSeniorDiscount);
               cellGrandSeniorDiscount.setCellStyle(numberTotalStyle);

               org.apache.poi.ss.usermodel.Cell cellGrandTotalAmount = totalRow.createCell(10);
               cellGrandTotalAmount.setCellValue(grandTotalAmountPay);
               cellGrandTotalAmount.setCellStyle(numberTotalStyle);

               // Auto-size columns
               for (int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
               }
               
               workbook.write(out);
               return out.toByteArray();
          } catch (Exception e) {
               throw new RuntimeException("Failed to generate Excel file: " + e.getMessage());
          }
     }

     @Override
     public byte[] generateCollectionReportDetails(Date startDate, Date endDate, Integer branchCode, Integer phase) {
          int bCode = (branchCode == null) ? -1 : branchCode;
          int pCode = (phase == null) ? -1 : phase;
          Date adjustedEndDate = getEndOfDay(endDate);

          java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection> data =
                    orCollectionRepo.getCollectionReportDetailsByDateRange(startDate, adjustedEndDate, bCode, pCode);

          // Group rows by OR No (preserving insertion order)
          java.util.LinkedHashMap<String, java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection>> grouped =
                    new java.util.LinkedHashMap<>();
          for (com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection row : data) {
               grouped.computeIfAbsent(row.getOrNo(), k -> new java.util.ArrayList<>()).add(row);
          }

          try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
               java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {

               org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Collection Report Details");
               org.apache.poi.ss.usermodel.CreationHelper ch = workbook.getCreationHelper();

               // ── Fonts ────────────────────────────────────────────────────────
               org.apache.poi.ss.usermodel.Font boldFont = workbook.createFont();
               boldFont.setBold(true);
               boldFont.setFontHeightInPoints((short) 11);

               org.apache.poi.ss.usermodel.Font boldSmFont = workbook.createFont();
               boldSmFont.setBold(true);
               boldSmFont.setFontHeightInPoints((short) 10);

               // ── Styles ───────────────────────────────────────────────────────
               // Report title
               org.apache.poi.ss.usermodel.CellStyle titleStyle = workbook.createCellStyle();
               titleStyle.setFont(boldFont);

               // Parent (summary) row – light blue fill
               org.apache.poi.ss.usermodel.CellStyle parentStyle = workbook.createCellStyle();
               parentStyle.setFont(boldSmFont);
               parentStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.PALE_BLUE.getIndex());
               parentStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
               parentStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Parent date style
               org.apache.poi.ss.usermodel.CellStyle parentDateStyle = workbook.createCellStyle();
               parentDateStyle.setFont(boldSmFont);
               parentDateStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.PALE_BLUE.getIndex());
               parentDateStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
               parentDateStyle.setDataFormat(ch.createDataFormat().getFormat("MMM dd, yyyy"));
               parentDateStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentDateStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentDateStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentDateStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Parent number style
               org.apache.poi.ss.usermodel.CellStyle parentNumStyle = workbook.createCellStyle();
               parentNumStyle.setFont(boldSmFont);
               parentNumStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.PALE_BLUE.getIndex());
               parentNumStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
               parentNumStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0.00"));
               parentNumStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentNumStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentNumStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               parentNumStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Child row – plain with borders
               org.apache.poi.ss.usermodel.CellStyle childStyle = workbook.createCellStyle();
               childStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Child number style
               org.apache.poi.ss.usermodel.CellStyle childNumStyle = workbook.createCellStyle();
               childNumStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0.00"));
               childNumStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childNumStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childNumStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
               childNumStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

               // Grand total row
               org.apache.poi.ss.usermodel.CellStyle totalStyle = workbook.createCellStyle();
               totalStyle.setFont(boldFont);
               totalStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);

               org.apache.poi.ss.usermodel.CellStyle totalNumStyle = workbook.createCellStyle();
               totalNumStyle.setFont(boldFont);
               totalNumStyle.setDataFormat(ch.createDataFormat().getFormat("#,##0.00"));
               totalNumStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalNumStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalNumStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               totalNumStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);

               // Table header style
               org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
               headerStyle.setFont(boldFont);
               headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
               headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
               headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);
               headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.MEDIUM);

               int rowIdx = 0;

               // ── Report meta rows ──────────────────────────────────────────────
               Map<Integer, String> branchMap = branchService.findAll().stream()
                    .filter(b -> b.getId() != null && b.getBranchCode() != null)
                    .collect(Collectors.toMap(BranchDto::getId, BranchDto::getBranchCode, (v1, v2) -> v1));
               Map<Integer, String> phaseMap = phaseService.findAll().stream()
                    .filter(p -> p.getId() != null && p.getPhase() != null)
                    .collect(Collectors.toMap(PhaseDto::getId, PhaseDto::getPhase, (v1, v2) -> v1));

               java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
               String branchLabel = (bCode == -1) ? "All" : branchMap.getOrDefault(bCode, String.valueOf(bCode));
               String phaseLabel  = (pCode == -1) ? "All" : phaseMap.getOrDefault(pCode, String.valueOf(pCode));

               org.apache.poi.ss.usermodel.Row r0 = sheet.createRow(rowIdx++);
               org.apache.poi.ss.usermodel.Cell c0 = r0.createCell(0);
               c0.setCellValue("Report Name: Collection Report (Details)");
               c0.setCellStyle(titleStyle);

               org.apache.poi.ss.usermodel.Row r1 = sheet.createRow(rowIdx++);
               org.apache.poi.ss.usermodel.Cell c1 = r1.createCell(0);
               c1.setCellValue("Report Date: " + sdf.format(startDate) + " to " + sdf.format(endDate));
               c1.setCellStyle(titleStyle);

               org.apache.poi.ss.usermodel.Row r2 = sheet.createRow(rowIdx++);
               org.apache.poi.ss.usermodel.Cell c2 = r2.createCell(0);
               c2.setCellValue("Branch Code: " + branchLabel + "   |   Phase: " + phaseLabel);
               c2.setCellStyle(titleStyle);

               rowIdx++; // blank row

               // ── Column headers ────────────────────────────────────────────────
               // PARENT columns  (cols 0-10)
               // CHILD  columns  (cols 11-24)
               String[] parentCols = {
                    "OR No", "OR Date", "Account No", "Alias",
                    "First Name", "Middle Name", "Last Name",
                    "Total Billing Amount", "Total Share Amount",
                    "Senior Discount", "Total Amount Pay"
               };
               String[] childCols = {
                    "Meter No", "Branch Code", "Phase",
                    "Reading Amt Paid", "Parts", "Membership",
                    "Tapping", "Calibration", "Reconnection",
                    "Sub Receivable", "Penalty", "Other Collection",
                    "Other Description", "Maintenance Fee",
                    "Senior Discount", "Septage Fee", "Total Amount",
                    "Remarks"
               };

               int totalCols = parentCols.length + childCols.length;
               org.apache.poi.ss.usermodel.Row hdrRow = sheet.createRow(rowIdx++);
               int col = 0;
               for (String h : parentCols) {
                    org.apache.poi.ss.usermodel.Cell cell = hdrRow.createCell(col++);
                    cell.setCellValue(h);
                    cell.setCellStyle(headerStyle);
               }
               for (String h : childCols) {
                    org.apache.poi.ss.usermodel.Cell cell = hdrRow.createCell(col++);
                    cell.setCellValue(h);
                    cell.setCellStyle(headerStyle);
               }

               // ── Grand-total accumulators ──────────────────────────────────────
               double gtBilling  = 0.0;
               double gtShare    = 0.0;
               double gtSenior   = 0.0;
               double gtTotal    = 0.0;

               // ── Data rows ─────────────────────────────────────────────────────
               for (java.util.Map.Entry<String, java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection>> entry : grouped.entrySet()) {
                    java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection> rows = entry.getValue();
                    com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection first = rows.get(0);

                    // ── Parent (summary) row ──────────────────────────────────────
                    org.apache.poi.ss.usermodel.Row parentRow = sheet.createRow(rowIdx++);
                    int pc = 0;

                    org.apache.poi.ss.usermodel.Cell pOrNo = parentRow.createCell(pc++);
                    pOrNo.setCellValue(first.getOrNo() != null ? first.getOrNo() : "");
                    pOrNo.setCellStyle(parentStyle);

                    org.apache.poi.ss.usermodel.Cell pDate = parentRow.createCell(pc++);
                    if (first.getOrDate() != null) { pDate.setCellValue(first.getOrDate()); }
                    pDate.setCellStyle(parentDateStyle);

                    org.apache.poi.ss.usermodel.Cell pAcct = parentRow.createCell(pc++);
                    pAcct.setCellValue(first.getAccountNo() != null ? first.getAccountNo() : "");
                    pAcct.setCellStyle(parentStyle);

                    org.apache.poi.ss.usermodel.Cell pAlias = parentRow.createCell(pc++);
                    pAlias.setCellValue(first.getCustomerAlias() != null ? first.getCustomerAlias() : "");
                    pAlias.setCellStyle(parentStyle);

                    org.apache.poi.ss.usermodel.Cell pFirst = parentRow.createCell(pc++);
                    pFirst.setCellValue(first.getFirstName() != null ? first.getFirstName() : "");
                    pFirst.setCellStyle(parentStyle);

                    org.apache.poi.ss.usermodel.Cell pMid = parentRow.createCell(pc++);
                    pMid.setCellValue(first.getMiddleName() != null ? first.getMiddleName() : "");
                    pMid.setCellStyle(parentStyle);

                    org.apache.poi.ss.usermodel.Cell pLast = parentRow.createCell(pc++);
                    pLast.setCellValue(first.getLastName() != null ? first.getLastName() : "");
                    pLast.setCellStyle(parentStyle);

                    double hBilling = first.getTotalBillingAmount() != null ? first.getTotalBillingAmount() : 0.0;
                    double hShare   = first.getTotalShareAmount()   != null ? first.getTotalShareAmount()   : 0.0;
                    double hSenior  = first.getSeniorDiscount()      != null ? first.getSeniorDiscount()      : 0.0;
                    double hTotal   = first.getTotalAmountPay()      != null ? first.getTotalAmountPay()      : 0.0;

                    org.apache.poi.ss.usermodel.Cell pBilling = parentRow.createCell(pc++);
                    pBilling.setCellValue(hBilling);
                    pBilling.setCellStyle(parentNumStyle);

                    org.apache.poi.ss.usermodel.Cell pShare = parentRow.createCell(pc++);
                    pShare.setCellValue(hShare);
                    pShare.setCellStyle(parentNumStyle);

                    org.apache.poi.ss.usermodel.Cell pSenior = parentRow.createCell(pc++);
                    pSenior.setCellValue(hSenior);
                    pSenior.setCellStyle(parentNumStyle);

                    org.apache.poi.ss.usermodel.Cell pTotal = parentRow.createCell(pc++);
                    pTotal.setCellValue(hTotal);
                    pTotal.setCellStyle(parentNumStyle);

                    // pad remaining child columns on parent row
                    for (int ci = pc; ci < totalCols; ci++) {
                         parentRow.createCell(ci).setCellStyle(parentStyle);
                    }

                    // accumulate grand totals
                    gtBilling += hBilling;
                    gtShare   += hShare;
                    gtSenior  += hSenior;
                    gtTotal   += hTotal;

                    // ── Child (detail) rows ───────────────────────────────────────
                    for (com.mahabang_parang.api_reports.model.interfaces.CollectionReportDetailInterfaceProjection detail : rows) {
                         org.apache.poi.ss.usermodel.Row childRow = sheet.createRow(rowIdx++);
                         int cc = 0;
                         // blank parent columns
                         for (int ci = 0; ci < parentCols.length; ci++) {
                              childRow.createCell(cc++).setCellStyle(childStyle);
                         }

                         org.apache.poi.ss.usermodel.Cell dMeter = childRow.createCell(cc++);
                         dMeter.setCellValue(detail.getMeterNo() != null ? detail.getMeterNo() : "");
                         dMeter.setCellStyle(childStyle);

                         org.apache.poi.ss.usermodel.Cell dBranch = childRow.createCell(cc++);
                         String branchName = (detail.getBranchCode() != null) ? branchMap.getOrDefault(detail.getBranchCode(), String.valueOf(detail.getBranchCode())) : "";
                         dBranch.setCellValue(branchName);
                         dBranch.setCellStyle(childStyle);

                          org.apache.poi.ss.usermodel.Cell dPhase = childRow.createCell(cc++);
                          String phaseName = (detail.getPhase() != null) ? phaseMap.getOrDefault(detail.getPhase(), String.valueOf(detail.getPhase())) : "";
                          dPhase.setCellValue(phaseName);
                          dPhase.setCellStyle(childStyle);

                         org.apache.poi.ss.usermodel.Cell dReading = childRow.createCell(cc++);
                         if (detail.getReadingAmountPaid() != null) dReading.setCellValue(detail.getReadingAmountPaid());
                         dReading.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dParts = childRow.createCell(cc++);
                         if (detail.getParts() != null) dParts.setCellValue(detail.getParts());
                         dParts.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dMembership = childRow.createCell(cc++);
                         if (detail.getMembership() != null) dMembership.setCellValue(detail.getMembership());
                         dMembership.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dTapping = childRow.createCell(cc++);
                         if (detail.getTapping() != null) dTapping.setCellValue(detail.getTapping());
                         dTapping.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dCalibration = childRow.createCell(cc++);
                         if (detail.getCalibration() != null) dCalibration.setCellValue(detail.getCalibration());
                         dCalibration.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dRecon = childRow.createCell(cc++);
                         if (detail.getReconnection() != null) dRecon.setCellValue(detail.getReconnection());
                         dRecon.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dSubRec = childRow.createCell(cc++);
                         if (detail.getSubReceivable() != null) dSubRec.setCellValue(detail.getSubReceivable());
                         dSubRec.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dPenalty = childRow.createCell(cc++);
                         if (detail.getPenalty() != null) dPenalty.setCellValue(detail.getPenalty());
                         dPenalty.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dOther = childRow.createCell(cc++);
                         if (detail.getOtherCollection() != null) dOther.setCellValue(detail.getOtherCollection());
                         dOther.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dOtherDesc = childRow.createCell(cc++);
                         dOtherDesc.setCellValue(detail.getOtherDescription() != null ? detail.getOtherDescription() : "");
                         dOtherDesc.setCellStyle(childStyle);

                         org.apache.poi.ss.usermodel.Cell dMaint = childRow.createCell(cc++);
                         if (detail.getMaintenanceFee() != null) dMaint.setCellValue(detail.getMaintenanceFee());
                         dMaint.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dSenior = childRow.createCell(cc++);
                         if (detail.getDetailSeniorDiscount() != null) dSenior.setCellValue(detail.getDetailSeniorDiscount());
                         dSenior.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dSeptage = childRow.createCell(cc++);
                         if (detail.getSeptageFee() != null) dSeptage.setCellValue(detail.getSeptageFee());
                         dSeptage.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dDetailTotal = childRow.createCell(cc++);
                         if (detail.getDetailTotalAmount() != null) dDetailTotal.setCellValue(detail.getDetailTotalAmount());
                         dDetailTotal.setCellStyle(childNumStyle);

                         org.apache.poi.ss.usermodel.Cell dRemarks = childRow.createCell(cc++);
                         dRemarks.setCellValue(detail.getRemarks() != null ? detail.getRemarks() : "");
                         dRemarks.setCellStyle(childStyle);
                    }
               }

               // ── Grand-total row ───────────────────────────────────────────────
               org.apache.poi.ss.usermodel.Row totalRow = sheet.createRow(rowIdx++);
               for (int ci = 0; ci < totalCols; ci++) {
                    totalRow.createCell(ci).setCellStyle(totalStyle);
               }

               org.apache.poi.ss.usermodel.Cell tLabel = totalRow.createCell(6);
               tLabel.setCellValue("GRAND TOTAL:");
               tLabel.setCellStyle(totalStyle);

               org.apache.poi.ss.usermodel.Cell tBilling = totalRow.createCell(7);
               tBilling.setCellValue(gtBilling);
               tBilling.setCellStyle(totalNumStyle);

               org.apache.poi.ss.usermodel.Cell tShare = totalRow.createCell(8);
               tShare.setCellValue(gtShare);
               tShare.setCellStyle(totalNumStyle);

               org.apache.poi.ss.usermodel.Cell tSenior = totalRow.createCell(9);
               tSenior.setCellValue(gtSenior);
               tSenior.setCellStyle(totalNumStyle);

               org.apache.poi.ss.usermodel.Cell tTotal = totalRow.createCell(10);
               tTotal.setCellValue(gtTotal);
               tTotal.setCellStyle(totalNumStyle);

               // ── Auto-size ─────────────────────────────────────────────────────
               for (int i = 0; i < totalCols; i++) {
                    sheet.autoSizeColumn(i);
               }

               workbook.write(out);
               return out.toByteArray();

          } catch (Exception e) {
               throw new RuntimeException("Failed to generate Collection Report Details Excel: " + e.getMessage(), e);
          }
     }

}
