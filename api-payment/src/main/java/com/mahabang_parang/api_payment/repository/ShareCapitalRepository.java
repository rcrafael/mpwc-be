package com.mahabang_parang.api_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.ShareCapitalEntity;

public interface ShareCapitalRepository extends JpaRepository<ShareCapitalEntity, String> {

     @Query("SELECT s FROM ShareCapitalEntity s WHERE s.orNo = :orNo")
     ShareCapitalEntity findByOrNo(@Param("orNo") String orNo);

}
