package com.mahabang_parang.api_billing.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_billing.model.entity.BillingEntity;

public interface BillingRepository extends JpaRepository<BillingEntity, String> {

     Page<BillingEntity> findAll(Pageable pageable);
     Page<BillingEntity> findByCustId(String custId, Pageable pageable);
     
}
