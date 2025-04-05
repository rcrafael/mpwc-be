package com.mahabang_parang.api_user.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerDto {

     private String id;
     private String nationalId;
     private String meterNo;
     private String alias;
     private String firstName;
     private String middleName;
     private String lastName;
     private int branchCode;
     private String address;
     private int gender;
     private String civilStatus;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateOfBirth;
     private String occupation;
     private String educationalLevel;
     private int numberOfDependent;
     private String religion;
     private float annualIncome;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateAccepted;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateUpdated;
     private String updatedBy;
     private int customerType;
     private int phase;
     private String accountNo;
     private String userId;
     private String tin;
}
