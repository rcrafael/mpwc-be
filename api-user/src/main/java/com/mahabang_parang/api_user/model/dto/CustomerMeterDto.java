package com.mahabang_parang.api_user.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
     private Integer previous;
     private Integer present;
     private Integer status;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateCreated;
     private String createdBy;
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     private Date dateUpdated;
     private String updatedBy;

}
