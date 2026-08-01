package com.mahabang_parang.api_user.model.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class CustomerMeterDto {

     private String id;
     private String customerId;
     private String meterNo;
     private String alias;
     private Integer branchCode;
     private Integer phase;
     private String location;
     private Integer previous;
     private Integer present;
     private Integer status;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateCreated;
     private String createdBy;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private LocalDateTime dateUpdated;
     private String updatedBy;
     private int customerType;
     private String serialNo;
}
