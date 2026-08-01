package com.mahabang_parang.api_user.model.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UpdateMeterStatusPayload {
     private String meterId;
     private Integer status;
}
