package com.mahabang_parang.api_billing.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_billing.model.dto.SeptageFeeRateDto;
import com.mahabang_parang.api_billing.model.entity.SeptageFeeRateEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeptageFeeRateMapper {

     SeptageFeeRateDto toDto(SeptageFeeRateEntity entity);
     SeptageFeeRateEntity toEntity(SeptageFeeRateDto dto);

     List<SeptageFeeRateDto> toDtoList(List<SeptageFeeRateEntity> entityList);
     List<SeptageFeeRateEntity> toEntityList(List<SeptageFeeRateDto> dtoList);
     
} 