package com.mahabang_parang.api_payment.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;

import com.mahabang_parang.api_payment.model.dto.PrintReceiptDto;

public interface ReceiptService {

     public byte[] generateExcelFromTemplate(PrintReceiptDto report) throws IOException;
     public void setCellValue(Sheet sheet, int rowIndex, int columnIndex, String value);
     public void setCellValue(Sheet sheet, int rowIndex, int columnIndex, double value);
     public void setCellFormula(Sheet sheet, int rowIndex, int columnIndex, String formula);
     public byte[] convertExcelToPdf(byte[] excelBytes) throws IOException;
}
