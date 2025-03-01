package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.entity.CustomerEntity;

public interface CustomerService {
     public List<CustomerEntity> getCustomers(int pageNo, int pageSize);
     public CustomerEntity getCustomer(Long id);
     public List<CustomerEntity> getCustomersByFirstName(String firstName);
}
