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


@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
public class CustomerEntity {

     // public Customer() {}

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     @Column(length = 36, unique = true)
     private String id;

     @Column(length = 12)
     private String nationalId;

     @Column(length = 20, nullable = false)
     private String meterNo;

     @Column(length = 100, nullable = false)
     private String alias;

     @Column(length = 50)
     private String firstName;

     @Column(length = 50, nullable = true)
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

     @Column(length = 50, nullable = true)
     private String occupation;

     @Column(length = 100, nullable = true)
     private String educationalLevel;
     
     @Column(nullable = true)
     private int numberOfDependent;

     @Column(length = 100, nullable = true)
     private String religion;

     @Column(nullable = false)
     private float annualIncome;

     @JsonIgnore
     @CreationTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateAccepted;

     @JsonIgnore
     @UpdateTimestamp
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateUpdated;

     @Column(length = 36, nullable = true)
     private String updatedBy;
     private int customerType;
     private int phase;

     @Column(length = 20)
     private String accountNo;

     @Column(length = 36, nullable = true)
     private String userId;


     public CustomerEntity(String id, String nationalId, String meterNo, String alias, String firstName,
               String middleName, String lastName, int branchCode, String address, int gender, String civilStatus,
               Date dateOfBirth, String occupation, String educationalLevel, int numberOfDependent, String religion,
               float annualIncome, Date dateAccepted, Date dateUpdated, String updatedBy, int customerType, int phase,
               String accountNo, String userId) {
          this.id = id;
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
          this.educationalLevel = educationalLevel;
          this.numberOfDependent = numberOfDependent;
          this.religion = religion;
          this.annualIncome = annualIncome;
          this.dateAccepted = dateAccepted;
          this.dateUpdated = dateUpdated;
          this.updatedBy = updatedBy;
          this.customerType = customerType;
          this.phase = phase;
          this.accountNo = accountNo;
          this.userId = userId;
     }

}
