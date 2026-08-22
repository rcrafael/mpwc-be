package com.mahabang_parang.api_user.service.impl;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.ApiResponse;
import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.model.entity.CustomerMeterEntity;
import com.mahabang_parang.api_user.model.mapper.CustomerMeterMapper;
import com.mahabang_parang.api_user.model.payload.UpdateMeterStatusPayload;
import com.mahabang_parang.api_user.repository.CustomerMeterRepository;
import com.mahabang_parang.api_user.service.CustomerMeterService;

@Service
public class CustomerMeterServiceImpl implements CustomerMeterService{

     private final CustomerMeterRepository custMeterRepo;

     private final CustomerMeterMapper mapper;

     CustomerMeterServiceImpl(CustomerMeterMapper mapper, CustomerMeterRepository custMeterRepo) {
          this.mapper = mapper;
          this.custMeterRepo = custMeterRepo;
     }

     @Override
     public ApiResponse addCustomerMeter(CustomerMeterDto customerMeter) {

          ApiResponse response = new ApiResponse();

          if(custMeterRepo.findByMeterNo(customerMeter.getMeterNo()) != null) {
               response.setStatusCode(HttpStatus.CONFLICT);
               response.setMessage("Meter Number already exists: " + customerMeter.getMeterNo());
               response.setResult(null);
               return response;
          }

          if(custMeterRepo.findBySerialNo(customerMeter.getSerialNo()) != null) {
               response.setStatusCode(HttpStatus.CONFLICT);
               response.setMessage("Serial Number already exists: " + customerMeter.getSerialNo());
               response.setResult(null);
               return response;
          }

          CustomerMeterEntity custMeter = new CustomerMeterEntity();
          
          custMeter.setCustomerId(customerMeter.getCustomerId());
          custMeter.setMeterNo(customerMeter.getMeterNo());
          custMeter.setSerialNo(customerMeter.getSerialNo());
          custMeter.setAlias(customerMeter.getAlias());
          custMeter.setBranchCode(customerMeter.getBranchCode());
          custMeter.setPhase(customerMeter.getPhase());
          custMeter.setCustomerType(customerMeter.getCustomerType());
          custMeter.setLocation(customerMeter.getLocation());
          custMeter.setStatus(customerMeter.getStatus() == null ? 7 : customerMeter.getStatus()); // default to 7 = Deactivated
          custMeter.setCreatedBy(customerMeter.getCreatedBy());
          custMeter.setCustomerType(customerMeter.getCustomerType()); // 1 - residential; 2 - commercial
          CustomerMeterDto newMeter = mapper.toDto(custMeterRepo.save(custMeter));

          response.setStatusCode(HttpStatus.CREATED);
          response.setMessage("success");
          response.setResult(newMeter);

          return response;
     }

     @Override
     public CustomerMeterDto updateCustomerMeter(CustomerMeterDto customerMeter) {
          

          CustomerMeterEntity custMeter = custMeterRepo.findByMeterNo(customerMeter.getMeterNo());
          
          custMeter.setSerialNo(customerMeter.getSerialNo());
          custMeter.setAlias(customerMeter.getAlias());
          custMeter.setBranchCode(customerMeter.getBranchCode());
          custMeter.setPhase(customerMeter.getPhase());
          custMeter.setCustomerType(customerMeter.getCustomerType());
          custMeter.setLocation(customerMeter.getLocation());
          custMeter.setStatus(customerMeter.getStatus());
          custMeter.setPrevious(customerMeter.getPrevious());
          custMeter.setPresent(customerMeter.getPresent());
          custMeter.setUpdatedBy(customerMeter.getUpdatedBy());
          custMeter.setDateUpdated(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
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
          List<CustomerMeterEntity> meters = custMeterRepo.findByCustomerIdAndStatus(customerId, 6);
          return mapper.toDtoList(meters);
     }

     @Override
     public CustomerMeterDto updateCustomerMeterStatus(UpdateMeterStatusPayload meterStatus) {
          CustomerMeterEntity entity = custMeterRepo.findById(meterStatus.getMeterId()).orElseThrow();
          entity.setStatus(meterStatus.getStatus());
          return mapper.toDto(custMeterRepo.save(entity));
     }

     private Integer getPreviousUsage(String meterNo) {
          Integer previousUsage = custMeterRepo.findPresentById(meterNo);
          return previousUsage == null ? 0 : previousUsage;
     }
     
}
