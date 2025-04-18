package com.mahabang_parang.api_billing.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_billing.model.PaginationRequest;
import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.dto.BillingWithCustomerInfoDto;
import com.mahabang_parang.api_billing.model.payload.UpdateBillingPayload;

@Service
public interface BillingService {
     
     public PaginationResponse<BillingDto> getBilling(int pageNumber, int pageSize) throws Exception;
     public PaginationResponse<BillingDto> getBillingByCustId(String customerId, int pageNumber, int pageSize) throws Exception;
     public List<BillingDto> getRecentBillingByCustomerId(String customerId) throws Exception;
     public BillingWithCustomerInfoDto getBillingWithCustomerInfoById(String billingId) throws Exception; 
     public BillingDto getBillingById(String billingId) throws Exception;
     public BillingDto create(BillingDto billing) throws Exception;
     public String generateReadingNo() throws Exception;
     public List<BillingDto> getUnpaidBillingByCustomerId(String customerId) throws Exception;
     public Integer updateBillingStatus(String readingNo, Integer newStatus) throws Exception;
     public BillingDto update(UpdateBillingPayload payload) throws Exception;
}
