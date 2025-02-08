package com.mahabang_parang.api_billing.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationResponse<T> {

     private List<?> data;
     private long totalRecordCount;
     private int pageNumber;
     private int pageSize;
     private int totalPages;

     public PaginationResponse(List<?> data, int pageNumber, int pageSize, int totalPages, long totalRecordCount) {
          this.data = data;
          this.pageNumber = pageNumber;
          this.pageSize = pageSize;
          this.totalPages = totalPages;
          this.totalRecordCount = totalRecordCount;
     }

}
