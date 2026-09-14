package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.model.entity.PhaseEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PhaseMapper {

     PhaseDto toDto(PhaseEntity entity);
     PhaseEntity toEntity(PhaseDto dto);

     List<PhaseDto> toDtoList(List<PhaseEntity> entityList);
     List<PhaseEntity> toEntityList(List<PhaseDto> dtoList);
}
