package com.mahabang_parang.api_payment.model.payload;

import java.util.List;

import com.mahabang_parang.api_payment.model.dto.OtherPaymentDetailsDto;
import com.mahabang_parang.api_payment.model.dto.OtherPaymentHeaderDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OtherPaymentPayload {
     private OtherPaymentHeaderDto header;
     private List<OtherPaymentDetailsDto> details;
}
