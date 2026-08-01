package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.model.payload.UpdateMeterStatusPayload;

public interface CustomerMeterService {

     ApiResponse addCustomerMeter(CustomerMeterDto customerMeter);
     CustomerMeterDto updateCustomerMeter(CustomerMeterDto customerMeter);
     CustomerMeterDto getCustomerMeterByMeterNo(String meterNo);
     List<CustomerMeterDto> getCustomerMeters();
     List<CustomerMeterDto> getCustomerMetersByCustomerId(String customerId);
     CustomerMeterDto updateCustomerMeterStatus(UpdateMeterStatusPayload meterStatus);

}
