package com.mahabang_parang.api_payment.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
     private HttpStatus statusCode;
     private String message;
     private Object result;
}
