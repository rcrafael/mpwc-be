package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.Customer;

public interface CustomerService {
     public List<Customer> getCustomers(int pageNo, int pageSize);
     public Customer getCustomer(Long id);
     public List<Customer> getCustomersByFirstName(String firstName);
}
