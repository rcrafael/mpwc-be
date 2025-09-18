package com.mahabang_parang.api_payment.model.response;

import java.util.List;

import com.mahabang_parang.api_payment.model.dto.OfficialReceiptDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderWithCustomerDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OtherPaymentResponse {

     OtherPaymentHeaderWithCustomerDto header;
     List<OfficialReceiptDetailsDto> details;

}
