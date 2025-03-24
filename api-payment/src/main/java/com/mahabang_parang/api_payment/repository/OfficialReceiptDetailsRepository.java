package com.mahabang_parang.api_payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.OfficialReceiptDetailsEntity;

public interface OfficialReceiptDetailsRepository extends JpaRepository<OfficialReceiptDetailsEntity, String> {

     @Query("SELECT o FROM OfficialReceiptDetailsEntity o WHERE o.orNo = :orNo")
     List<OfficialReceiptDetailsEntity> findByOrNo(@Param("orNo") String orNo);

}
