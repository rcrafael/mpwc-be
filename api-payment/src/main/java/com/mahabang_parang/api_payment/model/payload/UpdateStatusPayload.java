package com.mahabang_parang.api_payment.model.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateStatusPayload {

     private String id;
     private int status;

}
