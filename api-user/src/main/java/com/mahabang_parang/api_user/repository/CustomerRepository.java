package com.mahabang_parang.api_user.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_user.model.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String>{

     @Query("SELECT c.accountNo FROM CustomerEntity c WHERE c.accountNo LIKE :datePrefix% ORDER BY c.accountNo DESC LIMIT 1")
     String findLastAccoutNo(@Param("datePrefix") String datePrefix);

     @Query("SELECT c FROM CustomerEntity c WHERE c.id IN :ids")
     List<CustomerEntity> findByIds(@Param("ids") List<String> ids);

     List<CustomerEntity> findByFirstNameContaining(String firstName);
     Page<CustomerEntity> findByFirstNameContainingIgnoreCaseOrMiddleNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrAccountNoContainingIgnoreCaseOrAddressContainingIgnoreCase(
          String firstName, String middleName, String lastName, String accountNo, String Address, Pageable pageable
     );

     CustomerEntity findByAccountNo(String accountNo);
}    
