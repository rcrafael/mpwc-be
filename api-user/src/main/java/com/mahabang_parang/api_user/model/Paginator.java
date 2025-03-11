package com.mahabang_parang.api_user.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Paginator {
     private long totalRecordCount;
     private int pageNumber;
     private int pageSize;
     private int totalPages;

     public Paginator(int pageNumber, int pageSize, int totalPages, long totalRecordCount) {
          this.pageNumber = pageNumber;
          this.pageSize = pageSize;
          this.totalRecordCount = totalRecordCount;
          this.totalPages = totalPages;
     }
}
