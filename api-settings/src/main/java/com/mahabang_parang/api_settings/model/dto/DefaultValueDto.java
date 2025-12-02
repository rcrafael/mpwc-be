package com.mahabang_parang.api_settings.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DefaultValueDto {

     private int id;
     private Double maintenanceFee;
     private Double penaltyPercentage;
     private Double reconnectionFee;

}
