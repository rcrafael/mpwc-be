package com.mahabang_parang.api_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.ShareSavingsEntity;

public interface ShareSavingsRepository extends JpaRepository<ShareSavingsEntity, String> {

     @Query("SELECT s FROM ShareSavingsEntity s WHERE s.orNo = :orNo")
     ShareSavingsEntity findByOrNo(@Param("orNo") String orNo);

}
