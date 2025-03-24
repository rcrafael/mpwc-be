package com.mahabang_parang.api_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mahabang_parang.api_payment.model.entity.ShareCbuEntity;

public interface ShareCbuRepository extends JpaRepository<ShareCbuEntity, String> {

     @Query("SELECT s FROM ShareCbuEntity s WHERE s.orNo = :orNo")
     ShareCbuEntity findByOrNo(@Param("orNo")String orNo);

}
