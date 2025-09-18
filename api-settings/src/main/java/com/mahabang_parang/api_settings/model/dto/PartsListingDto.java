package com.mahabang_parang.api_settings.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PartsListingDto {
     private String id;
     private String description;
     private Double sellingPrice;
}
