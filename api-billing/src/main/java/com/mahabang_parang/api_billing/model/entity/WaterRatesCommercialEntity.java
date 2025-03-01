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
@Table(name = "Water_Rates_Commercial")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class WaterRatesCommercialEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false, unique = true)
     private String id;

     @Column(length = 36, nullable = false)
     private String groupRatingId;

     @Column(nullable = false)
     private Integer hierarchy;

     @Column(name = "from_m3", nullable = false)
     private Integer from;

     @Column(name = "to_m3")
     private Integer to;

     @Column(nullable = false)
     private Double ratingValue;

     @Column
     private String description;

     @Column
     private Boolean isBaseAmount;

     @Column(nullable = false)
     private Boolean isActive;

}
