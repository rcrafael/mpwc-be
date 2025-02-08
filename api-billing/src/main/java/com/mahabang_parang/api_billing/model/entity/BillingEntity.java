package com.mahabang_parang.api_billing.model.entity;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="reading")
@Getter
@Setter
@NoArgsConstructor
public class BillingEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 32, unique = true)
     private String id;

     @Column(length = 32)
     private String custId;

     @Column(length =  50)
     private String meterNo; 

     private Integer previous;

     private Integer present;
     @Column(name = "m3_used")
     private Integer m3Used;

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

     @Column(length = 50)
     private String createdBy;

     @JsonIgnore
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @CreationTimestamp
     private LocalDateTime createdDate;

     @Column(length = 50)
     private String updatedBy;

     @JsonIgnore
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @UpdateTimestamp
     private LocalDateTime dateUpdated;


}
