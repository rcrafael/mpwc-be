package com.mahabang_parang.api_billing.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationResponse<T> {

     private List<?> data;
     private Paginator paginator;

     public PaginationResponse(List<?> data, int pageNumber, int pageSize, int totalPages, long totalRecordCount) {
          this.data = data;
          this.paginator = new Paginator(pageNumber, pageSize, totalPages, totalRecordCount);
     }

}

