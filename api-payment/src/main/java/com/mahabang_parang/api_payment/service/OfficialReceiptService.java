package com.mahabang_parang.api_payment.service;

import java.util.List;

import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;

public interface OfficialReceiptService {
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptAll(int pageNo, int pageSize, String sortColumn) throws Exception;
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptByCustomerId(int pageNo, int pageSize, String customerId) throws Exception;
     public OfficialReceiptDto createOfficialReceipt(OfficiaReceiptPayload payload) throws Exception;
     public String generateOfficialReceiptNo() throws Exception;
     public PaginationResponse<OfficialReceiptHeaderWithCustomerDto> getOfficialReceiptWithCustomer(int pageNo, int pageSize, String sortColumn) throws Exception;
     public OfficialReceiptResponse getOfficialReceiptWithDetailsById(String orhId) throws Exception;
}
