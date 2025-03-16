package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptDetailsEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OfficialReceiptDetailsMapper {

     OfficialReceiptDetailsDto toDto(OfficialReceiptDetailsEntity entity);
     OfficialReceiptDetailsEntity toEntity(OfficialReceiptDetailsDto dto);

     List<OfficialReceiptDetailsDto> toDtoList(List<OfficialReceiptDetailsEntity> entity);
     List<OfficialReceiptDetailsEntity> toEntityList(List<OfficialReceiptDetailsDto> dto);

}
