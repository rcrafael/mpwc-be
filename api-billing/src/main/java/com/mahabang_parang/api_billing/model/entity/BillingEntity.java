package com.mahabang_parang.api_billing.model.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="reading")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class BillingEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, unique = true)
     private String id;

     @Column(name="reading_no")
     private String readingNo;

     @Column(length = 36)
     private String customerId;

     @Column(length =  50)
     private String meterNo; 

     private Integer previous;

     private Integer present;
     
     @Column(name = "m3_used")
     private Integer m3Used;

     private double rating;

     private double readingAmount;

     private double parts;

     private double totalAmount;

     private double maintenanceFee;

     private double lessAmount;

     private double septageFee;

     @Column(length = 250) 
     private String remarks;

     private Integer monthOf;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateFrom;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateTo;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dueDate;

     private Integer status;

     @Column(length = 36)
     private String createdBy;

     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @Column(nullable = false, updatable = false)
     private LocalDateTime createdDate;

     @Column(length = 36)
     private String updatedBy;

     // @UpdateTimestamp
     @Temporal(TemporalType.TIMESTAMP)
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @Column(nullable = true)
     private LocalDateTime dateUpdated;

}