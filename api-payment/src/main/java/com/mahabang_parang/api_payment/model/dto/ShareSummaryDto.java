package com.mahabang_parang.api_payment.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ShareSummaryDto {

     private String id;
     private String customerId;
     private Double totalShareCap;
     private Double totalCbu;
     private Double totalSavings;
     private Double totalDividend;
     private Double rate;
     private Double totalPatronageRefundWater;
     private Double totalPatronageRefundRefilling;
     private Date dateCreated;
     private String createdBy;
     private Date dateUpdated;
     private String updatedBy;

}
