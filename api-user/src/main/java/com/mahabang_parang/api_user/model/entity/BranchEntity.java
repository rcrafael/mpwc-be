package com.mahabang_parang.api_user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "branch")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BranchEntity {

     @Id
     @Column(name = "id", nullable = false)
     private Integer id;

     @Column(name = "branch_code", length = 50)
     private String branchCode;

     public Integer getBranchId() {
          return id;
     }

     public void setBranchId(Integer branchId) {
          this.id = branchId;
     }
}
