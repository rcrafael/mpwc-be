package com.mahabang_parang.api_reports.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
     private int pageNumber;
     private int pageSize;
}
