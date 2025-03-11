package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.dto.CustomerDto;

public interface CustomerService {
     public List<CustomerDto> getCustomers(int pageNo, int pageSize);
     public CustomerDto getCustomer(String id);
     public List<CustomerDto> getCustomersByFirstName(String firstName);
}
