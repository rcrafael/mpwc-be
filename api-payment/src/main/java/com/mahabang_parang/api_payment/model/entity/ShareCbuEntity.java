package com.mahabang_parang.api_payment.model.entity;

import java.util.Date;

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
@Table(name = "share_cbu")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShareCbuEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false)
     private String id;

     private String orNo;

     private String customerId;

     private Double amount;

     private Date entryDate;

     private Integer transactionType; //1 = deposit, 2 = withdraw
}
