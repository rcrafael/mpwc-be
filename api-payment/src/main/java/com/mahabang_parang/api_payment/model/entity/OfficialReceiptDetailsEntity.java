package com.mahabang_parang.api_payment.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "official_receipt_detail")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class OfficialReceiptDetailsEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false)
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

     @Column(length = 250)
     private String otherDescription;

     private Double maintenanceFee;

     private Double seniorDiscount;

     private Double septageFee;

     private Double totalAmount;

     @Column(length = 250)
     private String remarks;

     @Column(length = 36, nullable = true)
     private String partId;



}
