package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mahabang_parang.api_payment.model.dto.ShareCbuDto;
import com.mahabang_parang.api_payment.model.entity.ShareCbuEntity;
import com.mahabang_parang.api_payment.model.mapper.ShareCbuMapper;
import com.mahabang_parang.api_payment.repository.ShareCbuRepository;
import com.mahabang_parang.api_payment.service.ShareCbuService;

public class ShareCbuServiceImpl implements ShareCbuService {

     @Autowired
     private ShareCbuRepository cbuRepo;

     @Autowired
     private ShareCbuMapper cbuMapper;
     

     @Override
     public ShareCbuDto create(ShareCbuDto cbu) throws Exception {
          ShareCbuEntity c = cbuRepo.save(cbuMapper.toEntity(cbu));
          return cbuMapper.toDto(c);
     }

}
