package com.mahabang_parang.api_billing.model.payload;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateBillingPayload {
     
     private String id;
     private String readingNo;
     private Integer monthOf;
     private Date dateFrom;
     private Date dateTo;
     private Date dueDate;
     private Integer previous;
     private Integer present;
     private Integer m3Used;
     private double readingAmount;
     private double parts;
     private double maintenanceFee;
     private double septageFee;
     private double totalAmount;
}
