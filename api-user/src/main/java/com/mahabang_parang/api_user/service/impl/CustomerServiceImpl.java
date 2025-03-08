package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.model.entity.CustomerEntity;
import com.mahabang_parang.api_user.model.mapper.CustomerMapper;
import com.mahabang_parang.api_user.repository.CustomerRepository;
import com.mahabang_parang.api_user.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{

     @Autowired
     private CustomerRepository custRepository;
     @Autowired
     private CustomerMapper mapper;

     @Override
     public List<CustomerDto> getCustomers(int pageNo, int pageSize){
          Pageable pageable = PageRequest.of(pageNo, pageSize);
          return mapper.toDtoList(custRepository.findAll(pageable).get().toList());
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

}
