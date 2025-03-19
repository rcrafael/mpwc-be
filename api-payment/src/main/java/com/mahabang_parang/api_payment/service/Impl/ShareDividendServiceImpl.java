package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.mahabang_parang.api_payment.model.dto.ShareDividendDto;
import com.mahabang_parang.api_payment.model.entity.ShareDividendEntity;
import com.mahabang_parang.api_payment.model.mapper.ShareDividendMapper;
import com.mahabang_parang.api_payment.repository.ShareDividendRepository;
import com.mahabang_parang.api_payment.service.ShareDividentService;

public class ShareDividendServiceImpl implements ShareDividentService {

     @Autowired
     private ShareDividendRepository dividendRepo;

     @Autowired
     private ShareDividendMapper dividendMapper;

     @Override
     public ShareDividendDto create(ShareDividendDto dividend) throws Exception {
          ShareDividendEntity d = dividendRepo.save(dividendMapper.toEntity(dividend));
          return dividendMapper.toDto(d);
     }

}
