package com.mahabang_parang.api_payment.service;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;

@Service
public interface ShareCapitalService {

     public ShareCapitalDto create(ShareCapitalDto capital) throws Exception;

}
