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
public class OfficialReceiptDto {

     private OfficialReceiptHeaderDto header;
     private List<OfficialReceiptDetailsDto> details;
     private ShareCapitalDto shareCapital;
     private ShareCbuDto cbu;
     private ShareSavingsDto savings;
     
}
