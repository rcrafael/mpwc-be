package com.mahabang_parang.api_payment.service.Impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.mahabang_parang.api_payment.model.dto.PrintReceiptDto;
import com.mahabang_parang.api_payment.service.ReceiptService;

@Service
public class ReceiptServiceImpl implements ReceiptService {

     private static final String TEMPLATE_PATH = "templates/or_template.xlsx";

     public byte[] generateExcelFromTemplate(PrintReceiptDto receipt) throws IOException {
        // Load the template
        InputStream templateStream = new ClassPathResource(TEMPLATE_PATH).getInputStream();
        
        try (Workbook workbook = new XSSFWorkbook(templateStream);
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
          
          // Get the first sheet (or specify by name)
          Sheet sheet = workbook.getSheetAt(0);
          FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
          
          SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");

          // Row 0, Column 1 (B1) - Customer Name
          setCellValue(sheet, 2, 0, receipt.getMeterNo());
          
          if (receipt.getOrDate() != null) {
               setCellValue(sheet, 2, 3, dateFormatter.format(receipt.getOrDate()));
          }
          
          setCellValue(sheet, 7,2,receipt.getMonthOf());
          setCellValue(sheet, 3, 1, receipt.getCustomer().getLastName() + ", " 
                                                            + receipt.getCustomer().getFirstName() + " " 
                                                            + receipt.getCustomer().getMiddleName());
          setCellValue(sheet, 4, 0, receipt.getCustomer().getAddress()); 

          setCellValue(sheet, 7,4,receipt.getReadingAmountPaid());
          setCellValue(sheet, 8, 4,receipt.getPenalty());
          setCellValue(sheet, 9, 4, receipt.getParts());
          setCellValue(sheet, 10, 4, receipt.getMembership());
          setCellValue(sheet, 11, 4, receipt.getTapping());
          setCellValue(sheet, 12, 4, receipt.getReconnection());
          setCellValue(sheet, 13, 4, receipt.getCalibration());
          setCellValue(sheet, 14, 4, receipt.getSeptageFee());
          setCellValue(sheet, 15, 4, 0); //sharecap
          setCellValue(sheet, 16, 4, receipt.getSubReceivable());
          setCellFormula(sheet,18,4,"SUM(E8:E17)");
          setCellValue(sheet,20,4,0);
          setCellValue(sheet,21,4,0);
          setCellValue(sheet,22,4,0);
          setCellValue(sheet,23,4,0);
          setCellValue(sheet,24,4,0);
          setCellValue(sheet,25,4,0);
          setCellValue(sheet,26,1,receipt.getOtherDescription());
          setCellValue(sheet,26,4,receipt.getOtherCollection());
          
          setCellValue(sheet, 28, 4, receipt.getSeniorDiscount());
          setCellFormula(sheet,29,4,"SUM(E8:E17,E21:F27)-E29");
          
          evaluator.evaluateAll();

            // Write workbook to byte array
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } finally {
            templateStream.close();
        }
    }
    
    /**
     * Helper method to set cell value without losing formatting
     */
    public void setCellValue(Sheet sheet, int rowIndex, int columnIndex, String value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        
        // Set value without changing style
        if (value != null) {
            cell.setCellValue(value);
        }
    }
    
    /**
     * Helper method to set numeric cell value
     */
    public void setCellValue(Sheet sheet, int rowIndex, int columnIndex, double value) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
          row = sheet.createRow(rowIndex);
        }
        
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
          cell = row.createCell(columnIndex);
        }
        
        cell.setCellValue(value);
    }

    public void setCellFormula(Sheet sheet, int rowIndex, int columnIndex, String formula) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        
        // Set formula (without "=" prefix)
        cell.setCellFormula(formula);
    }

    public byte[] convertExcelToPdf(byte[] excelBytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            // Create converter instance
            IConverter converter = LocalConverter.builder()
                    .build();
            
            // Convert Excel to PDF
            converter.convert(inputStream)
                    .as(DocumentType.XLSX)
                    .to(outputStream)
                    .as(DocumentType.PDF)
                    .execute();
            
            converter.shutDown();
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            throw new IOException("Failed to convert Excel to PDF", e);
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Override
    public byte[] convertExcelToPdfWithSpire(byte[] excelBytes) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(excelBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            com.spire.xls.Workbook workbook = new com.spire.xls.Workbook();
            workbook.loadFromStream(inputStream);

            if (workbook.getWorksheets().getCount() > 0) {
                workbook.getWorksheets().get(0).getPageSetup().isFitToPage(true);
            }

            workbook.saveToStream(outputStream, com.spire.xls.FileFormat.PDF);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException("Failed to convert Excel to PDF using Spire.XLS", e);
        }
    }

}
