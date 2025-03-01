package com.mahabang_parang.api_billing.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WaterRatesDto {

     private String id;
     private String groupRatingId;
     private Integer order;
     private String operator;
     private Double ratingValue;
     private String description;
     private boolean isBaseAmount;
     private boolean isActive;

}
