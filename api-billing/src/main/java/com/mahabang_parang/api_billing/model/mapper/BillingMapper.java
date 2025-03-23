package com.mahabang_parang.api_billing.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_billing.model.dto.BillingDto;
import com.mahabang_parang.api_billing.model.entity.BillingEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BillingMapper {

     BillingDto toDto(BillingEntity billingEntity);
     BillingEntity toEntity(BillingDto billingDto);

     List<BillingDto> toDtoList(List<BillingEntity> billingEntity);
     List<BillingEntity> toEntityList(List<BillingDto> billingDto);

     // Page<BillingDto> toDtoPage(Page<BillingEntity> billingEntity);
     // Page<BillingEntity> toEntityPage(Page<BillingDto> billingDto);

}
