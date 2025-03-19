package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mahabang_parang.api_payment.model.dto.ShareCapitalDto;
import com.mahabang_parang.api_payment.model.entity.ShareCapitalEntity;
import com.mahabang_parang.api_payment.model.mapper.ShareCapitalMapper;
import com.mahabang_parang.api_payment.repository.ShareCapitalRepository;
import com.mahabang_parang.api_payment.service.ShareCapitalService;

public class ShareCapitalServiceImpl implements ShareCapitalService {

     @Autowired
     private ShareCapitalRepository shareCapRepo;

     @Autowired
     private ShareCapitalMapper mapper;

     @Override
     public ShareCapitalDto create(ShareCapitalDto shareCapital) throws Exception {
          ShareCapitalEntity shareCap = shareCapRepo.save(mapper.toEntity(shareCapital));
          return mapper.toDto(shareCap);
     }

}
