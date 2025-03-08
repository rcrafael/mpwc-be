package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.CustomerDto;
import com.mahabang_parang.api_user.model.entity.CustomerEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

     CustomerDto toDto(CustomerEntity entity);
     CustomerEntity toEntity(CustomerDto dto);

     List<CustomerDto> toDtoList(List<CustomerEntity> entity);
     List<CustomerEntity> toEntityList(List<CustomerDto> dto);

}
