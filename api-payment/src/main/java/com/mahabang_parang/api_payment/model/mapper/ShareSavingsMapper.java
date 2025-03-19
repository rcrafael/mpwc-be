package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;
import com.mahabang_parang.api_payment.model.entity.ShareSavingsEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShareSavingsMapper {

     ShareSavingsDto toDto(ShareSavingsEntity entity);
     ShareSavingsEntity toEntity(ShareSavingsDto dto);

     List<ShareSavingsDto> toDtoList(List<ShareSavingsEntity> entity);
     List<ShareSavingsEntity> toEntityList(List<ShareSavingsDto> dto);

}
