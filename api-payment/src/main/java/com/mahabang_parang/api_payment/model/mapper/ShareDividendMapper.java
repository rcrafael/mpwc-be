package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.ShareDividendDto;
import com.mahabang_parang.api_payment.model.entity.ShareDividendEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShareDividendMapper {

     ShareDividendDto toDto(ShareDividendEntity entity);
     ShareDividendEntity toEntity(ShareDividendDto dto);

     List<ShareDividendDto> toDtoList(List<ShareDividendEntity> entities);
     List<ShareDividendEntity> toEntityList(List<ShareDividendDto> dtos);

}
