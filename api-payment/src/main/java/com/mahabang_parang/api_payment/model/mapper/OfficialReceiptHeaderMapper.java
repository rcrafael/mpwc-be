package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.entity.OfficialReceiptHeaderEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OfficialReceiptHeaderMapper {

     OfficialReceiptHeaderDto toDto(OfficialReceiptHeaderEntity entity);
     OfficialReceiptHeaderEntity toEntity(OfficialReceiptHeaderDto dto);

     List<OfficialReceiptHeaderDto> toDtoList(List<OfficialReceiptHeaderEntity> entity);
     List<OfficialReceiptHeaderEntity> toEntityList(List<OfficialReceiptHeaderDto> dto);

}
