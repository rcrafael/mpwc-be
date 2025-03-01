package com.mahabang_parang.api_billing.service;

import java.util.List;

import com.mahabang_parang.api_billing.model.dto.WaterRatesDto;

public interface WaterRatesService {

     List<WaterRatesDto> getActiveWaterRatesResidentials();
     List<WaterRatesDto> getActiveWaterRatesCommercial();
     List<WaterRatesDto> getAllWaterRatesResidential();
     List<WaterRatesDto> getAllWaterRatesCommercial();

     List<WaterRatesDto> updateWaterRatesResidentials(List<WaterRatesDto> payload);
     List<WaterRatesDto> updateWaterRatesCommercial(List<WaterRatesDto> payload);

     List<WaterRatesDto> addWaterRatesResidentials(List<WaterRatesDto> payload);
     List<WaterRatesDto> addWaterRatesCommercial(List<WaterRatesDto> payload);

     List<WaterRatesDto> getWaterRatesCommercialByGroupId(String groupId);
     List<WaterRatesDto> getWaterRatesResidentialByGroupId(String groupId);

     List<WaterRatesDto> setActiveWaterRatesResidential(String groupRatingId, Boolean isActive);
     List<WaterRatesDto> setActiveWaterRatesCommercial(String groupRatingId, Boolean isActive);

}
