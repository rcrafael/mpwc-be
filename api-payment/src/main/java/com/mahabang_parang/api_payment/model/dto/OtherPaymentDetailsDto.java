package com.mahabang_parang.api_payment.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class OtherPaymentDetailsDto {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false, unique = true)
     private String id;

     private String orNo;

     private String partId;

     private Double parts; //amount for parts

     private String OtherDescription;

     private Double totalAmount;

     private String remarks;

}
