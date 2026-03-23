package com.mahabang_parang.api_reports.service.impl;

import java.util.Date;

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
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.repository.CustomerRepository;

@Service
public class CollectionReportServiceImpl implements CollectionReportService {

     private final ORCollectionRepository orCollectionRepo;
     private final BillingRepository billingRepo;
     private final CustomerRepository customerRepo;
     private final CustomerMeterRepository customerMeterRepo;
     private final OfficialReceiptHeaderRepository orHeaderRepo;

     public CollectionReportServiceImpl(ORCollectionRepository _orCollectionRepo,
                                        BillingRepository _billingRepo,
                                        CustomerRepository _customerRepo,
                                        CustomerMeterRepository _customerMeterRepo,
                                        OfficialReceiptHeaderRepository _orHeaderRepo
     ) {
          this.orCollectionRepo = _orCollectionRepo;
          this.billingRepo = _billingRepo;
          this.customerRepo = _customerRepo;
          this.customerMeterRepo = _customerMeterRepo;
          this.orHeaderRepo = _orHeaderRepo;
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
          ShareCapCbuSavingsCollectionInterfaceProjection result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionByCustomerIdAndDateRange(customerId, startDate, endDate);
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

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByCustomerIdAndDateRange(customerId, startDate, endDate, pageable);
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

          Page<ShareCapCbuSavingsCollectionListInterfaceProjection> result = 
                              orCollectionRepo.getShareCapCbuSavingsCollectionListByDateRange(startDate, endDate, pageable);
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
          
          DashboardCardDataDto result = new DashboardCardDataDto();
          Long userCount = customerRepo.count();
          Long activeMeterCount = customerMeterRepo.countByStatus(6); //6 = Active
          Integer totalCubicMeterUsage =  billingRepo.totalCubicMeterUsageByDate(startDate, endDate);
          Double totalUnpaidBillingAmount = billingRepo.sumOfUnpaidBillingByDate(startDate, endDate);
          Double totalOfficialReceiptAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, endDate, false);
          Double totalOtherCollectionAmount = orHeaderRepo.sumOfOfficialReceiptByDate(startDate, endDate, true);
          double totalShareCapAmount = orCollectionRepo.sumOfShareCapitalByDateRange(startDate, endDate);
          double totalCbuAmount = orCollectionRepo.sumOfCbuByDateRange(startDate, endDate);

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
     public byte[] generateCollectionReportExcel(Date startDate, Date endDate) {
          java.util.List<com.mahabang_parang.api_reports.model.interfaces.CollectionReportInterfaceProjection> data = 
                    orCollectionRepo.getCollectionReportByDateRange(startDate, endDate);
          
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

}
