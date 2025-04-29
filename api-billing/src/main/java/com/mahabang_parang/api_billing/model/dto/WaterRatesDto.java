package com.mahabang_parang.api_billing.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties
public class WaterRatesDto {

     private String id;
     private String groupRatingId;
     private Integer hierarchy;
     private Integer from;
     private Integer to;
     private Double ratingValue;
     private String description;
     private Boolean isBaseAmount;
     private Boolean isActive;

}
