package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.CustomerMeterDto;
import com.mahabang_parang.api_user.model.entity.CustomerMeterEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMeterMapper {

     CustomerMeterEntity toEntity(CustomerMeterDto dto);
     CustomerMeterDto toDto(CustomerMeterEntity entity);

     List<CustomerMeterDto> toDtoList(List<CustomerMeterEntity> entity);
     List<CustomerMeterEntity> toEntityList(List<CustomerMeterEntity> entity);

}
