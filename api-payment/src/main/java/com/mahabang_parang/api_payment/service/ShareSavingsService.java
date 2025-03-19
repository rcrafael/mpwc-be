package com.mahabang_parang.api_payment.service;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;

@Service
public interface ShareSavingsService {

     ShareSavingsDto create(ShareSavingsDto savings) throws Exception;

}
