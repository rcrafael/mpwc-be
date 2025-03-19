package com.mahabang_parang.api_payment.service;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.SharePatronageRefundDto;

@Service
public interface SharePatronageRefundService {

     SharePatronageRefundDto create(SharePatronageRefundDto patronageRefund) throws Exception;
}
