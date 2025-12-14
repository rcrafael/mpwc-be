package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.ShareSummaryDto;
import com.mahabang_parang.api_payment.model.entity.ShareSummaryEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ShareSummaryMapper {

     ShareSummaryDto toDto(ShareSummaryEntity entity);

     ShareSummaryEntity toEntity(ShareSummaryDto dto);

     List<ShareSummaryDto> toDtoList(List<ShareSummaryEntity> entities);

     List<ShareSummaryEntity> toEntityList(List<ShareSummaryDto> dtos);

}
