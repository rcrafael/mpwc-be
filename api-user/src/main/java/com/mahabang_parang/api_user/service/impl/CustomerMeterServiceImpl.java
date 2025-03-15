package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.model.entity.CustomerMeterEntity;
import com.mahabang_parang.api_user.model.mapper.CustomerMeterMapper;
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.service.CustomerMeterService;

@Service
public class CustomerMeterServiceImpl implements CustomerMeterService{

     @Autowired
     private CustomerMeterRepository custMeterRepo;

     @Autowired
     private CustomerMeterMapper mapper;

     @Override
     public CustomerMeterDto addCustomerMeter(CustomerMeterDto customerMeter) {

          CustomerMeterEntity custMeter = new CustomerMeterEntity();
          
          custMeter.setCustomerId(customerMeter.getCustomerId());
          custMeter.setMeterNo(customerMeter.getMeterNo());
          custMeter.setAlias(customerMeter.getAlias());
          custMeter.setStatus(customerMeter.getStatus() == null ? 7 : customerMeter.getStatus()); // default to 7 = Deactivated
          custMeter.setCreatedBy(customerMeter.getCreatedBy());
          CustomerMeterDto newMeter = mapper.toDto(custMeterRepo.save(custMeter));

          return newMeter;
     }

     @Override
     public CustomerMeterDto updateCustomerMeter(CustomerMeterDto customerMeter) {
          

          CustomerMeterEntity custMeter = custMeterRepo.findByMeterNo(customerMeter.getMeterNo());
          
          custMeter.setAlias(customerMeter.getAlias());
          custMeter.setStatus(customerMeter.getStatus());
          custMeter.setPrevious(customerMeter.getPrevious());
          custMeter.setPresent(customerMeter.getPresent());
          CustomerMeterDto newMeter = mapper.toDto(custMeterRepo.save(custMeter));

          return newMeter;

     }

     @Override
     public CustomerMeterDto getCustomerMeterByMeterNo(String meterNo) {
          CustomerMeterEntity custMeter = custMeterRepo.findByMeterNo(meterNo);
          // custMeter.setPrevious(getPreviousUsage(meterNo));
          return mapper.toDto(custMeter);
     }

     @Override
     public List<CustomerMeterDto> getCustomerMeters() {
          List<CustomerMeterEntity> meters = custMeterRepo.findAll();
          return mapper.toDtoList(meters);
     }

     @Override
     public List<CustomerMeterDto> getCustomerMetersByCustomerId(String customerId) {
          List<CustomerMeterEntity> meters = custMeterRepo.findByCustomerId(customerId);
          return mapper.toDtoList(meters);
     }

     private Integer getPreviousUsage(String meterNo) {
          Integer previousUsage = custMeterRepo.findPresentById(meterNo);
          return previousUsage == null ? 0 : previousUsage;
     }
     
}
