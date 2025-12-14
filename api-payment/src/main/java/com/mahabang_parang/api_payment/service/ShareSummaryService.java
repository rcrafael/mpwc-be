package com.mahabang_parang.api_payment.service;

import com.mahabang_parang.api_payment.model.dto.ShareSummaryDto;

public interface ShareSummaryService {

     public ShareSummaryDto createOrUpdate(ShareSummaryDto summary) throws Exception;
     public ShareSummaryDto getByCustomerId(String customerId) throws Exception;

}
