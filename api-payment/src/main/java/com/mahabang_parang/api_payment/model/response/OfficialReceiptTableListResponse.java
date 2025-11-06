package com.mahabang_parang.api_payment.model.response;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OfficialReceiptTableListResponse {

     private String id;
     private String orNo;
     private String customerId;
     private String accountNo;
     private String firstName;
     private String lastName;
     private String middleName;
     private String address;
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
     private String outsideCustomerAddress;
     private Boolean isDeleted;

}
