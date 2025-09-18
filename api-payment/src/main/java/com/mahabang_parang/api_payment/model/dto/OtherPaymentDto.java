package com.mahabang_parang.api_payment.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OtherPaymentDto {

     private OfficialReceiptHeaderDto header;
     private List<OfficialReceiptDetailsDto> details;

}
