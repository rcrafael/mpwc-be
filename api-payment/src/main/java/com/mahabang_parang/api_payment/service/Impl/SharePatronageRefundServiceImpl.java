package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mahabang_parang.api_payment.model.dto.SharePatronageRefundDto;
import com.mahabang_parang.api_payment.model.entity.SharePatronageRefundEntity;
import com.mahabang_parang.api_payment.model.mapper.SharePatronageRefundMapper;
import com.mahabang_parang.api_payment.repository.SharePatronageRefundRepository;
import com.mahabang_parang.api_payment.service.SharePatronageRefundService;

public class SharePatronageRefundServiceImpl implements SharePatronageRefundService {

     @Autowired
     private SharePatronageRefundRepository patRefRepo;

     @Autowired
     private SharePatronageRefundMapper patRefMapper;


     @Override
     public SharePatronageRefundDto create(SharePatronageRefundDto patronageRefund) throws Exception {
          SharePatronageRefundEntity patRef = patRefRepo.save(patRefMapper.toEntity(patronageRefund));
          return patRefMapper.toDto(patRef);
     }

}
