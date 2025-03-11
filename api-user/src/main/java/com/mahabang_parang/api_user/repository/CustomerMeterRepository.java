package com.mahabang_parang.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_user.model.entity.CustomerMeterEntity;
import java.util.List;


public interface CustomerMeterRepository extends JpaRepository<CustomerMeterEntity, String> {


     @Query("SELECT m.present FROM CustomerMeterEntity m WHERE m.meterNo = :meterNo")
     Integer findPresentById(@Param("meterNo") String id);

     CustomerMeterEntity findByMeterNo(String meterNo);
     List<CustomerMeterEntity> findByCustomerId(String customerId);
     
}
