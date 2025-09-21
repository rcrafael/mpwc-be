package com.mahabang_parang.api_payment.model.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "official_receipt_header")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OfficialReceiptHeaderEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false, unique = true)
     private String id;

     @Column(nullable = false, length = 20)
     private String orNo;

     @Column(nullable = false, length = 36)
     private String customerId;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date orDate;
     
     private Double totalBillingAmount;

     private Double totalShareAmount;

     private Double totalAmountPay;

     private Double seniorDiscount;

     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @Column(nullable = false, updatable = false)
     private Date entryDate;

     @Column(length = 36)
     private String createdBy;

     @Column(length = 50)
     private String checkNo;

     @Column(length = 100)
     private String checkBank;

     private Date checkDate;

     @Column(nullable = false)
     private Integer status;

     private Boolean isPartSale;

     private Boolean isOutsideCollection;

     @Column(length = 50)
     private String outsideCustomerName;

     @Column(length = 250)
     private String outsideCustomerAddress;
}
