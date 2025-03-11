package com.mahabang_parang.api_billing.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class BillingDto {

     private String id;

     private String readingNo;

     private String customerId;

     private String meterNo; 

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


