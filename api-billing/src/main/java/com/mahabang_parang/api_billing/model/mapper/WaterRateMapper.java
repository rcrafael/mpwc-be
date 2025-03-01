package com.mahabang_parang.api_billing.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_billing.model.dto.WaterRatesDto;
import com.mahabang_parang.api_billing.model.entity.WaterRatesCommercialEntity;
import com.mahabang_parang.api_billing.model.entity.WaterRatesResidentialEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WaterRateMapper {

     WaterRatesDto toCommercialRateDto(WaterRatesCommercialEntity waterRateEntity);
     WaterRatesDto toResedentialRateDto(WaterRatesResidentialEntity waterRateEntity);
     WaterRatesCommercialEntity toCommercialRateEntity(WaterRatesDto waterRateDto);
     WaterRatesResidentialEntity toResidentialRateEntity(WaterRatesDto waterRateDto);

     List<WaterRatesDto> toCommercialRateDtoList(List<WaterRatesCommercialEntity> waterRateEntity);
     List<WaterRatesDto> toResidentialRateDtoList(List<WaterRatesResidentialEntity> waterRateEntity);

     List<WaterRatesCommercialEntity> toCommercialEntityList(List<WaterRatesDto> waterRateDto);
     List<WaterRatesResidentialEntity> toResidentialEntityList(List<WaterRatesDto> waterRateDto);

}
