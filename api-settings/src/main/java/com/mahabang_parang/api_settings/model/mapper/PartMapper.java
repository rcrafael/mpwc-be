package com.mahabang_parang.api_settings.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_settings.model.dto.PartDto;
import com.mahabang_parang.api_settings.model.entity.PartEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartMapper {

     PartEntity toEntity(PartDto dto);
     PartDto toDto(PartEntity entity);

     List<PartEntity> toEntityList(List<PartDto> dtos);
     List<PartDto> toDtoList(List<PartEntity> entities);

}
