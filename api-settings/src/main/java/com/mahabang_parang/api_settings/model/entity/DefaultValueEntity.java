package com.mahabang_parang.api_settings.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="default_value")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DefaultValueEntity {

     @Id
     @Column
     private int id;

     @Column
     private double maintenanceFee;

     @Column
     private double penaltyPercentage;

     @Column
     private double reconnectionFee;

}
