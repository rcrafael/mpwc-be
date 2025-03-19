package com.mahabang_parang.api_payment.model.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShareCbuDto {
private String id;

     private String orNo;

     private String customerId;

     private Double amount;

     private Date entryDate;

     private Integer transactionType; //1 = deposit, 2 = withdraw
}
