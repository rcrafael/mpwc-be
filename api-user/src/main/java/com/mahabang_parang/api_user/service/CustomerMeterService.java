package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;

public interface CustomerMeterService {

     CustomerMeterDto addCustomerMeter(CustomerMeterDto customerMeter);
     CustomerMeterDto updateCustomerMeter(CustomerMeterDto customerMeter);
     CustomerMeterDto getCustomerMeterByMeterNo(String meterNo);
     List<CustomerMeterDto> getCustomerMeters();
     List<CustomerMeterDto> getCustomerMetersByCustomerId(String customerId);
     

}
