package com.mahabang_parang.api_billing.service;


import java.util.List;


import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.dto.BillingWithCustomerInfoDto;
import com.mahabang_parang.api_billing.model.payload.UpdateBillingPayload;
import com.mahabang_parang.api_billing.model.response.BillingTableListResponse;

public interface BillingService {
     
     public PaginationResponse<BillingTableListResponse> getBilling(int pageNumber, int pageSize) throws Exception;
     public PaginationResponse<BillingDto> getBillingByCustId(String customerId, int pageNumber, int pageSize) throws Exception;
     public List<BillingDto> getRecentBillingByCustomerId(String customerId) throws Exception;
     public BillingWithCustomerInfoDto getBillingWithCustomerInfoById(String billingId) throws Exception; 
     public BillingDto getBillingById(String billingId) throws Exception;
     public BillingDto getBillingByReadingNo(String readingNo) throws Exception;
     public BillingDto create(BillingDto billing) throws Exception;
     public String generateReadingNo() throws Exception;
     public List<BillingDto> getUnpaidBillingByCustomerId(String customerId) throws Exception;
     public Integer updateBillingStatus(String readingNo, Integer newStatus) throws Exception;
     public BillingDto update(UpdateBillingPayload payload) throws Exception;

     public PaginationResponse<BillingTableListResponse> searchBilling(String keyword, int pageNumber, int pageSize) throws Exception;
     
}
