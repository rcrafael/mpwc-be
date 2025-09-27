package com.mahabang_parang.api_payment.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OtherPaymentHeaderDto {
     private String id;
     private String orNo;
     private String customerId;
     private Date orDate;
     private Double totalBillingAmount;
     private Double totalAmountPay;
     private Date entryDate;
     private String createdBy;
     private String checkNo;
     private String checkBank;
     private Date checkDate;
     private Integer status;
     private Boolean isPartSale;
     private Boolean isOutsideCollection;
     private String outsideCustomerName;
     private String outsideCustomerAddress;
     private Boolean isDeleted;
}
