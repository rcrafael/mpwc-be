package com.mahabang_parang.api_reports.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ShareCapCbuSavingsCollectionRequestDto {

     private String customerId;
     private String startDate;
     private String endDate;
     private int pageSize;
     private int pageNumber;

}
