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
public class OfficialReceiptHeaderWithCustomerDto {
     private String id;
     private String orNo;
     private String customerId;
     private CustomerDto customer;
     private Date orDate;
     private Double totalBillingAmount;
     private Double totalShareAmount;
     private Double totalAmountPay;
     private Double seniorDiscount;
     private Date entryDate;
     private String createdBy;
     private String checkNo;
     private String checkBank;
     private Date checkDate;
     private Integer status;
     private Boolean isPartSale;
     private Boolean isOutsideCollection;
     private String outsideCustomerName;
     private String partId;
     private String outsideCustomerAddress;
     private Boolean isDeleted;
}
