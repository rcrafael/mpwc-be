package com.mahabang_parang.api_user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BranchDto {

     private Integer id;
     private String branchCode;

     public Integer getBranchId() {
          return id;
     }

     public void setBranchId(Integer branchId) {
          this.id = branchId;
     }
}
