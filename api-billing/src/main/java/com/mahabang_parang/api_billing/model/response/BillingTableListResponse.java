package com.mahabang_parang.api_billing.model.response;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString
@NoArgsConstructor
public class BillingTableListResponse {

     private String id;
     private String readingNo;

     // customer info
     private String customerId;
     private String accountNo;
     private String firstName;
     private String lastName;
     private String middleName;
     private String address;

     //Meter info
     private String meterNo;
     private String alias;
     private Integer branchCode;

     private Integer previous;
     private Integer present;
     private Integer m3Used;
     private double rating;
     private double readingAmount;
     private double parts;
     private double totalAmount;
     private double maintenanceFee;
     private double lessAmount;
     private double septageFee;
     private String remarks;
     private Integer monthOf;
     private Date dateFrom;
     private Date dateTo;
     private Date dueDate;
     private Integer status;
     private String createdBy;
     private LocalDateTime createdDate;
     private String updatedBy;
     private LocalDateTime dateUpdated;
}
