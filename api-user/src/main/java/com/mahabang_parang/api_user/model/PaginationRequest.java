package com.mahabang_parang.api_user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationRequest {
     private int pageNumber;
     private int pageSize;
}
