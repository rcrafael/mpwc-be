package com.mahabang_parang.api_payment.service;

import java.util.List;

import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;

public interface OfficialReceiptService {
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptAll(int pageNo, int pageSize, String sortColumn) throws Exception;
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptByCustomerId(int pageNo, int pageSize, String customerId) throws Exception;
     public OfficialReceiptHeaderDto createOfficialReceipt(OfficialReceiptHeaderDto header, List<OfficialReceiptDetailsDto> details) throws Exception;
     public String generateOfficialReceiptNo() throws Exception;
}
