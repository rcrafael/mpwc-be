package com.mahabang_parang.api_billing.model.entity;

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
@Table(name = "Water_Rates_Residential")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class WaterRatesResidentialEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false, unique = true)
     private String id;

     @Column(length = 36, nullable = false)
     private String groupRatingId;

     @Column(nullable = false)
     private Integer order;

     @Column(nullable = false)
     private String operator;

     @Column(nullable = false)
     private Double ratingValue;

     @Column
     private String description;

     @Column
     private boolean isBaseAmount;

     @Column(nullable = false)
     private boolean isActive;
}
