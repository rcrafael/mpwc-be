package com.mahabang_parang.api_payment.model.dto;

import java.util.Date;

import com.mahabang_parang.api_user.model.dto.CustomerDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrintReceiptDto {
     
     private String id;
     private String orNo;
     private String customerId;
     private CustomerDto customer;
     private String alias;
     private String outsideCustomerName;
     private String outsideCustomerAddress;
     private Date orDate;
     private String monthOf; //MMM -Jan, Feb, Mar, etc
     private Double totalBillingAmount;
     private Double totalShareAmount;
     private Double totalAmountPay;
    
     private String meterNo;
     private Double readingAmountPaid; //WaterService 
     private Double parts;
     private Double membership;
     private Double tapping;
     private Double calibration;
     private Double reconnection;
     private Double subReceivable;
     private Double penalty;
     private Double otherCollection;
     private String otherDescription;
     private Double maintenanceFee; //Calibration
     private Double septageFee;
     private Double seniorDiscount; //LessOthers
     private Double totalAmount;
     private String remarks;
}
