package com.mahabang_parang.api_payment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
     private int pageNumber;
     private int pageSize;
}
