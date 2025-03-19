package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;
import com.mahabang_parang.api_payment.model.entity.ShareCapitalEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShareCapitalMapper {

     ShareCapitalDto toDto(ShareCapitalEntity entity);
     ShareCapitalEntity toEntity(ShareCapitalDto dto);

     List<ShareCapitalDto> toDtoList(List<ShareCapitalEntity> entity);
     List<ShareCapitalEntity> toEntityList(List<ShareCapitalDto> dto);

}
