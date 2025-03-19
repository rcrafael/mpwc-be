package com.mahabang_parang.api_payment.service;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;

@Service
public interface ShareCbuService {

     ShareCbuDto create(ShareCbuDto cbu) throws Exception;


}
