package com.mahabang_parang.api_billing.service;

import com.mahabang_parang.api_billing.model.dto.SeptageFeeRateDto;

public interface SeptageFeeRateService {

     public SeptageFeeRateDto update(SeptageFeeRateDto payload) throws Exception;
     public SeptageFeeRateDto getRates() throws Exception;
}
