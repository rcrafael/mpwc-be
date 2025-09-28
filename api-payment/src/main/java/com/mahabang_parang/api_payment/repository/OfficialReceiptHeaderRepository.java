package com.mahabang_parang.api_payment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;

public interface OfficialReceiptHeaderRepository extends JpaRepository<OfficialReceiptHeaderEntity, String>{

     @Query("SELECT o.orNo FROM OfficialReceiptHeaderEntity o WHERE o.orNo LIKE :datePrefix% ORDER BY o.orNo DESC LIMIT 1")
     String findLastOrNo(@Param("datePrefix") String datePrefix);

     Page<OfficialReceiptHeaderEntity> findByCustomerIdAndIsPartSaleAndIsDeleted(String customerId, Boolean isPartSale, Boolean isDeleted, Pageable pageable);

     Page<OfficialReceiptHeaderEntity> findByIsPartSaleAndIsDeleted(Boolean isPartSale, Boolean isDeleted, Pageable pageable);

     // Page<OfficialReceiptHeaderEntity> findByCustomerIdContainingIgnoreCaseOrOrNoContainingIgnoreCase(String customerId, String orNo, Pageable pageable);

     @Query("SELECT o FROM OfficialReceiptHeaderEntity o WHERE LOWER(o.customerId) LIKE LOWER(CONCAT('%', :customerId, '%')) OR LOWER(o.orNo) LIKE LOWER(CONCAT('%', :orNo, '%')) AND o.isPartSale = :isPartSale AND o.isDeleted = false")
     Page<OfficialReceiptHeaderEntity> searchByCustomerIdOrOrNo(
        @Param("customerId") String customerId,
        @Param("orNo") String orNo,
        @Param("isPartSale") Boolean isPartSale,
        Pageable pageable);

}
