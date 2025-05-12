package com.mahabang_parang.api_settings.model.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PartDto {

     private String id;

     private String description;

     private String measurement;

     private Double unitCost;

     private Double sellingPrice;

     private String updatedBy;

     private LocalDateTime dateUpdated;


}
