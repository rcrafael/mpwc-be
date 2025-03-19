package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;
import com.mahabang_parang.api_payment.model.entity.ShareCbuEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShareCbuMapper {

     ShareCbuDto toDto(ShareCbuEntity entity);
     ShareCbuEntity toEntity(ShareCbuDto entity);
     
     List<ShareCbuDto> toDtoList(List<ShareCbuEntity> entities);
     List<ShareCbuEntity> toEntityList(List<ShareCbuDto> dto);
     
}
