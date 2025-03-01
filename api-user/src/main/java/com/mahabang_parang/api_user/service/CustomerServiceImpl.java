package com.mahabang_parang.api_user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.entity.CustomerEntity;
import com.mahabang_parang.api_user.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{

     @Autowired
     private CustomerRepository custRepository;
     
     @Override
     public List<CustomerEntity> getCustomers(int pageNo, int pageSize){
          Pageable pageable = PageRequest.of(pageNo, pageSize);
          return custRepository.findAll(pageable).get().toList();
     }

     @Override
     public CustomerEntity getCustomer(Long id) {
          System.out.println("customerId:" + id);
          return custRepository.findById(id).get();
     }

     @Override
     public List<CustomerEntity> getCustomersByFirstName(String firstName) {
          return custRepository.findByFirstNameContaining(firstName);
     }

}
