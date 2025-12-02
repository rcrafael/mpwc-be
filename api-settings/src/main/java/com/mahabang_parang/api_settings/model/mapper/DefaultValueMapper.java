package com.mahabang_parang.api_settings.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_settings.model.dto.DefaultValueDto;
import com.mahabang_parang.api_settings.model.entity.DefaultValueEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DefaultValueMapper {

     DefaultValueDto toDto(DefaultValueEntity entity);
     DefaultValueEntity toEntity(DefaultValueDto dto);

}
