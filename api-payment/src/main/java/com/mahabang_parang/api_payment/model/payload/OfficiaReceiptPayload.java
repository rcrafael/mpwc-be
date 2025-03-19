package com.mahabang_parang.api_payment.model.payload;

import java.util.List;

import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OfficialReceiptHeaderDto;
import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;
import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;
import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OfficiaReceiptPayload {

     private OfficialReceiptHeaderDto header;
     private List<OfficialReceiptDetailsDto> details;
     private ShareCapitalDto shareCapital;
     private ShareCbuDto cbu;
     private ShareSavingsDto savings;

}
