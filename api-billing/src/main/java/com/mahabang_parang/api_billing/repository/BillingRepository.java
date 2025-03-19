package com.mahabang_parang.api_billing.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_billing.model.entity.BillingEntity;

public interface BillingRepository extends JpaRepository<BillingEntity, String> {

     @Query("SELECT b.readingNo FROM BillingEntity b WHERE b.readingNo LIKE :datePrefix% ORDER BY b.readingNo DESC LIMIT 1")
     String findLastReadingNo(@Param("datePrefix") String datePrefix);

     @Query("SELECT b from BillingEntity b WHERE b.customerId = :customerId AND b.status = :status ORDER BY b.createdDate DESC LIMIT :limit")
     List<BillingEntity> findByCustomerIdAndStatus(@Param("customerId") String customerId, @Param("status") int status, @Param("limit") int limit);

     @Modifying
     @Transactional
     @Query("UPDATE BillingEntity b SET b.status = :newStatus WHERE b.readingNo = :readingNo")
     Integer updateBillingStatus(@Param("newStatus") Integer newStatus, @Param("readingNo")  String readingNo);

     Page<BillingEntity> findAll(Pageable pageable);
     Page<BillingEntity> findByCustomerId(String custId, Pageable pageable);
     
}
