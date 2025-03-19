package com.mahabang_parang.api_payment.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OfficialReceiptDetailsDto {
     private String id;
     private String orNo;
     private String readingNo;
     private String meterNo;
     private Double readingAmountPaid;
     private Double parts;
     private Double membership;
     private Double tapping;
     private Double calibration;
     private Double reconnection;
     private Double subReceivable;
     private Double penalty;
     private Double otherCollection;
     private String otherDescription;
     private Double maintenanceFee;
     private Double seniorDiscount;
     private Double septageFee;
     private Double totalAmount;
     private String remarks;
     private Integer billingStatus;
}
