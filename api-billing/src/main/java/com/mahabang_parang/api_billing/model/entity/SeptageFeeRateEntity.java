package com.mahabang_parang.api_billing.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "septage_fee_rates")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SeptageFeeRateEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = true, unique = true)
     private String id;
     @Column(nullable = false)
     private Double minimumZeroTo10;
     @Column(nullable = false)
     private Double m3To11;
     @Column(nullable = false)
     private Double m3To12;
     @Column(nullable = false)
     private Double m3To13;
     @Column(nullable = false)
     private Double m3To14;
     @Column(nullable = false)
     private Double m3To15;
     @Column(nullable = false)
     private Double m3To16;
     @Column(nullable = false)
     private Double m3To17;
     @Column(nullable = false)
     private Double m3To18;
     @Column(nullable = false)
     private Double m3To19;
     @Column(nullable = false)
     private Double m3To20;
     @Column(nullable = false)
     private Double m3To21;
     @Column(nullable = false)
     private Double m3To22;
     @Column(nullable = false)
     private Double m3To23;
     @Column(nullable = false)
     private Double m3To24;
     @Column(nullable = false)
     private Double m3To25;
     @Column(nullable = false)
     private Double m3To26;
     @Column(nullable = false)
     private Double m3To27;
     @Column(nullable = false)
     private Double m3To28;
     @Column(nullable = false)
     private Double m3To29;
     @Column(nullable = false)
     private Double m3To30;
     


}
