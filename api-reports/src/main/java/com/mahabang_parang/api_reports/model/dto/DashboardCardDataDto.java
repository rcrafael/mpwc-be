package com.mahabang_parang.api_reports.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DashboardCardDataDto {

     private Long userCount;
     private Long activeMeterCount;
     private Integer totalCubicMeterUsage;
     private Double totalUnpaidBillingAmount;
     private Double totalOfficialReceiptAmount;
     private Double totalOtherCollectionAmount;
     private Double totalShareCapAmount;
     private Double totalCbuAmount;

}
