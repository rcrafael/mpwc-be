package com.mahabang_parang.api_user.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.mahabang_parang.api_user.ApiUserApplication;
import com.mahabang_parang.api_user.model.PaginationResponse;
import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.model.entity.CustomerEntity;
import com.mahabang_parang.api_user.model.mapper.CustomerMapper;
import com.mahabang_parang.api_user.repository.CustomerRepository;
import com.mahabang_parang.api_user.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final ApiUserApplication apiUserApplication;

     @Autowired
     private CustomerRepository custRepository;
     @Autowired
     private CustomerMapper mapper;

    CustomerServiceImpl(ApiUserApplication apiUserApplication) {
        this.apiUserApplication = apiUserApplication;
    }

     @Override
     public PaginationResponse<CustomerDto> getCustomers(int pageNo, int pageSize){
          PageRequest pageable = PageRequest.of(pageNo, pageSize);

          Page<CustomerEntity> customer =custRepository.findAll(pageable);
          
          return new PaginationResponse<>(
               mapper.toDtoList(customer.getContent()),
               customer.getNumber(),
               customer.getSize(),
               customer.getTotalPages(),
               customer.getTotalElements()
          );
     }

     @Override
     public CustomerDto getCustomer(String id) {
          System.out.println("customerId:" + id);
          return mapper.toDto(custRepository.findById(id).get());
     }

     @Override
     public List<CustomerDto> getCustomersByFirstName(String firstName) {
          return mapper.toDtoList(custRepository.findByFirstNameContaining(firstName));
     }

     @Override
     public CustomerDto addCustomer(CustomerDto customer) {
          CustomerEntity cust = mapper.toEntity(customer);
          cust.setAccountNo(generateControlNumber());
          return mapper.toDto(custRepository.save(cust));
     }

     @Override
     public String generateAccountNo() {
          return generateControlNumber();
     }

     private String generateControlNumber() {
          String datePrefix  = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
          String lastAccoutNo = custRepository.findLastAccoutNo("C"+datePrefix);

          int sequence = 1;
          if(lastAccoutNo != null) {
               int lastSequence = Integer.parseInt(lastAccoutNo.substring(9));
               sequence = lastSequence + 1;
          }

          return 'C' + datePrefix + String.format("%05d", sequence);
     }


}
