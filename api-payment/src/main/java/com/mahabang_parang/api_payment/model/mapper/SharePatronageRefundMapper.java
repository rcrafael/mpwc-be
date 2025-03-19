package com.mahabang_parang.api_payment.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_payment.model.dto.SharePatronageRefundDto;
import com.mahabang_parang.api_payment.model.entity.SharePatronageRefundEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharePatronageRefundMapper {

     SharePatronageRefundDto toDto(SharePatronageRefundEntity entity);
     SharePatronageRefundEntity toEntity(SharePatronageRefundDto dto);

     List<SharePatronageRefundDto> toDtoList(List<SharePatronageRefundEntity> entities);
     List<SharePatronageRefundEntity> toEntityList(List<SharePatronageRefundDto> dtos);

}
