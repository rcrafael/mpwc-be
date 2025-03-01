package com.mahabang_parang.api_billing.service;


import java.util.Optional;

import org.springframework.data.domain.Page;

import com.mahabang_parang.api_billing.model.PaginationRequest;
import com.mahabang_parang.api_billing.model.PaginationResponse;
import com.mahabang_parang.api_billing.model.dto.BillingDto;

public interface BillingService {
     
     public PaginationResponse<BillingDto> getBilling(int pageNumber, int pageSize) throws Exception;
     public PaginationResponse<BillingDto> getBillingByCustId(String customerId, int pageNumber, int pageSize) throws Exception;
     public BillingDto getBillingById(String billingId) throws Exception;
     public BillingDto create(BillingDto billing) throws Exception;

}
