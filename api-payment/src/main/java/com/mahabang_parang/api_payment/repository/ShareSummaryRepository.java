package com.mahabang_parang.api_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_payment.model.entity.ShareSummaryEntity;

public interface ShareSummaryRepository extends JpaRepository<ShareSummaryEntity, String> {

     ShareSummaryEntity findByCustomerId(String customerId);

}
