package com.mahabang_parang.api_user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_user.model.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String>{

     @Query("SELECT c.accountNo FROM CustomerEntity c WHERE c.accountNo LIKE :datePrefix% ORDER BY c.accountNo DESC LIMIT 1")
     String findLastAccoutNo(@Param("datePrefix") String datePrefix);

     List<CustomerEntity> findByFirstNameContaining(String firstName);
     CustomerEntity findByAccountNo(String accountNo);
}    
