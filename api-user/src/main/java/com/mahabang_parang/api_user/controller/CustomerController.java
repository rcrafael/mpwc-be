package com.mahabang_parang.api_user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mahabang_parang.api_user.model.Customer;
import com.mahabang_parang.api_user.service.CustomerService;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/user")
public class CustomerController {
     @Autowired
     private CustomerService service;

     @GetMapping("customer/get/{pageNo}/{pageSize}")
     public List<Customer> getCustomers(@PathVariable int pageNo, @PathVariable int pageSize) {
          return service.getCustomers(pageNo,pageSize);
     }

     @GetMapping("customer/{id}")
     public Customer getCustomer(@PathVariable() Long id) {
          return service.getCustomer(id);
     }  

     @GetMapping("customer/first-name")
     public List<Customer> getCustomerbyFirstname(@RequestParam String firstName) {
         return service.getCustomersByFirstName(firstName);
     }
     

}
