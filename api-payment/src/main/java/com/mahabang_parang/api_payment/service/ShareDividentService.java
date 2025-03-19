package com.mahabang_parang.api_payment.service;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.ShareDividendDto;

@Service
public interface ShareDividentService {

     ShareDividendDto create(ShareDividendDto dividend) throws Exception;

}
