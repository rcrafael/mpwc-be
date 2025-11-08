package com.mahabang_parang.api_user.model.entity;

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
import lombok.ToString;

@Entity
@Table(name = "customer_meter")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerMeterEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, unique = true)
     private String id;

     @Column(length = 36, nullable = false)
     private String customerId;

     @Column(nullable = false)
     private String meterNo;
     
     @Column(nullable = true)
     private String alias;

     @Column(nullable = false)
     private Integer branchCode;

     @Column(nullable = false)
     private Integer phase;

     @Column(nullable = true)
     private String location;
     
     private Integer previous;

     private Integer present;

     private Integer status;

     @JsonIgnore
     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateCreated;

     @Column(length = 36, nullable = false)
     private String createdBy;

     @JsonIgnore
     @UpdateTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     @Column(nullable = true)
     private Date dateUpdated;

     @Column(length = 36, nullable = true)
     private String updatedBy;

     private int customerType;

     private String serialNo;
}
