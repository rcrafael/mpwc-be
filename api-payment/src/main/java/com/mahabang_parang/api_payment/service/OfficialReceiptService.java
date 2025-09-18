package com.mahabang_parang.api_payment.service;

import com.mahabang_parang.api_payment.model.PaginationResponse;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderWithCustomerDto;
import com.mahabang_parang.api_payment.model.payload.OfficiaReceiptPayload;
import com.mahabang_parang.api_payment.model.payload.OtherPaymentPayload;
import com.mahabang_parang.api_payment.model.response.OfficialReceiptResponse;
import com.mahabang_parang.api_payment.model.response.OtherPaymentResponse;

public interface OfficialReceiptService {
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptAll(int pageNo, int pageSize, String sortColumn) throws Exception;
     public PaginationResponse<OfficialReceiptHeaderDto> getOfficialReceiptByCustomerId(int pageNo, int pageSize, String customerId) throws Exception;
     public OfficialReceiptDto createOfficialReceipt(OfficiaReceiptPayload payload) throws Exception;
     public String generateOfficialReceiptNo() throws Exception;
     public PaginationResponse<OfficialReceiptHeaderWithCustomerDto> getOfficialReceiptWithCustomer(int pageNo, int pageSize, String sortColumn) throws Exception;
     public OfficialReceiptResponse getOfficialReceiptWithDetailsById(String orhId) throws Exception;


     public OtherPaymentDto createOtherCollection(OtherPaymentPayload payload) throws Exception;
     public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionAll(int pageNo, int pageSize, String sortColumn) throws Exception;
     public PaginationResponse<OtherPaymentHeaderDto> getOtherCollectionByCustomerId(int pageNo, int pageSize, String customerId) throws Exception;
     public PaginationResponse<OtherPaymentHeaderWithCustomerDto> getOtherCollectionWithCustomer(int pageNo, int pageSize, String sortColumn) throws Exception;
     public OtherPaymentResponse getOtherCollectionWithDetailsById(String orhId) throws Exception;
}
