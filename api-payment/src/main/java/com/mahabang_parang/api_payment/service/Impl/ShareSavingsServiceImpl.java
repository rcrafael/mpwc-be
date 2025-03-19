package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mahabang_parang.api_payment.model.dto.ShareSavingsDto;
import com.mahabang_parang.api_payment.model.entity.ShareSavingsEntity;
import com.mahabang_parang.api_payment.model.mapper.ShareSavingsMapper;
import com.mahabang_parang.api_payment.repository.ShareSavingsRepository;
import com.mahabang_parang.api_payment.service.ShareSavingsService;

public class ShareSavingsServiceImpl implements ShareSavingsService {

     @Autowired
     private ShareSavingsRepository savingsRepo;

     @Autowired
     private ShareSavingsMapper savingsMapper;

     @Override
     public ShareSavingsDto create(ShareSavingsDto savings) throws Exception {
          ShareSavingsEntity saving = savingsRepo.save(savingsMapper.toEntity(savings));
          return savingsMapper.toDto(saving);
     }
}
