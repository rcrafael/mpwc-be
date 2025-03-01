package com.mahabang_parang.api_user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_user.model.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>{
     List<CustomerEntity> findByFirstNameContaining(String firstName);
}
