package com.mahabang_parang.api_payment.model.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "share_summary")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ShareSummaryEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false, unique = true)
     private String id;

     @Column(nullable = false, length = 36)
     private String customerId;
     
     private Double totalShareCap;
     private Double totalCbu;
     private Double totalSavings;
     private Double totalDividend;
     private Double rate;
     private Double totalPatronageRefundWater;
     private Double totalPatronageRefundRefilling;
     
     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateCreated;

     private String createdBy;

     @UpdateTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateUpdated;

     private String updatedBy;

}
