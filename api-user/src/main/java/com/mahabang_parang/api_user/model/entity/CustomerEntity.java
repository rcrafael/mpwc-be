package com.mahabang_parang.api_user.model.entity;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class CustomerEntity {

     // public Customer() {}

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     @Column(length = 32, unique = true)
     private Long id;

     @Column(length = 12)
     private String nationalId;

     @Column(length = 20)
     private String meterNo;

     @Column(length = 100)
     private String alias;

     @Column(length = 50)
     private String firstName;

     @Column(length = 50)
     private String middleName;

     @Column(length = 50)
     private String lastName;

     private int branchCode;

     @Column(length = 255)
     private String address;

     private int gender;

     @Column(length = 20)
     private String civilStatus;

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateOfBirth;

     @Column(length = 50)
     private String occupation;

     private int numberOfDependent;

     @Column(length = 100)
     private String religion;

     private float annualIncome;

     @JsonIgnore
     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateAccepted;

     @JsonIgnore
     @UpdateTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateUpdated;

     @Nullable
     private Integer updatedBy;
     private int customerType;
     private int phase;

     @Column(length = 20)
     private String accountNo;

     private Integer userId;

     public CustomerEntity(String nationalId, String meterNo, String alias, String firstName, String middleName,
               String lastName,
               int branchCode, String address, int gender, String civilStatus, Date dateOfBirth,
               String occupation, int numberOfDependent, String religion, float annualIncome, Integer updatedBy, 
               int customerType, int phase, String accountNo, Integer userId) {
          this.nationalId = nationalId;
          this.meterNo = meterNo;
          this.alias = alias;
          this.firstName = firstName;
          this.middleName = middleName;
          this.lastName = lastName;
          this.branchCode = branchCode;
          this.address = address;
          this.gender = gender;
          this.civilStatus = civilStatus;
          this.dateOfBirth = dateOfBirth;
          this.occupation = occupation;
          this.numberOfDependent = numberOfDependent;
          this.religion = religion;
          this.annualIncome = annualIncome;
          this.updatedBy = updatedBy;
          this.customerType = customerType;
          this.phase = phase;
          this.accountNo = accountNo;
          this.userId = userId;
     }

}
