package com.mahabang_parang.api_billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_billing.model.entity.WaterRatesCommercialEntity;

public interface WaterRatesCommercialRepository extends JpaRepository<WaterRatesCommercialEntity, String>{

     List<WaterRatesCommercialEntity> findByActive(boolean active);
     List<WaterRatesCommercialEntity> findByGroupRatingId(String groupRatingId);
}
