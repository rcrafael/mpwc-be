package com.mahabang_parang.api_billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_billing.model.entity.WaterRatesResidentialEntity;

public interface WaterRatesResidentialRepository extends JpaRepository<WaterRatesResidentialEntity, String> {

     List<WaterRatesResidentialEntity> findByActive(boolean active);
     List<WaterRatesResidentialEntity> findByGroupRatingId(String groupRatingId);
     
}
