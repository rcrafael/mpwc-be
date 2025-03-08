package com.mahabang_parang.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_user.model.entity.CustomerMeterEntity;
import java.util.List;


public interface CustomerMeterRepository extends JpaRepository<CustomerMeterEntity, String> {

     CustomerMeterEntity findByMeterNo(String meterNo);
     List<CustomerMeterEntity> findByCustomerId(String customerId);
     
}
