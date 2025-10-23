package com.mahabang_parang.api_settings.model.entity;

import java.util.Date;

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
@Table(name = "parts")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PartEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, nullable = false)
     private String id;

     @Column(length = 250)
     private String description;

     @Column(length = 50)
     private String measurement;

     private Double unitCost;

     private Double sellingPrice;

     @Column(length = 36)
     private String updatedBy;

     @Temporal(TemporalType.TIMESTAMP)
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @Column(nullable = true)
     private Date dateUpdated;

}
