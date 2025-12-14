package com.mahabang_parang.api_payment.service.Impl;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_payment.model.dto.ShareSummaryDto;
import com.mahabang_parang.api_payment.model.entity.ShareSummaryEntity;
import com.mahabang_parang.api_payment.model.mapper.ShareSummaryMapper;
import com.mahabang_parang.api_payment.repository.ShareSummaryRepository;
import com.mahabang_parang.api_payment.service.ShareSummaryService;


@Service
public class ShareSummaryServiceImpl implements ShareSummaryService {

     private final ShareSummaryRepository _shareSummaryRepo;
     private final ShareSummaryMapper _shareSummaryMapper;

     public ShareSummaryServiceImpl(
          ShareSummaryRepository shareSummaryRepo,
          ShareSummaryMapper shareSummaryMapper
     ) {
          this._shareSummaryRepo = shareSummaryRepo;
          this._shareSummaryMapper = shareSummaryMapper;
     }

     @Override
     public ShareSummaryDto createOrUpdate(ShareSummaryDto summary) throws Exception {
          ShareSummaryEntity entity = _shareSummaryRepo.save(_shareSummaryMapper.toEntity(summary));
          return _shareSummaryMapper.toDto(entity);
     }

     @Override
     public ShareSummaryDto getByCustomerId(String customerId) throws Exception {
          ShareSummaryEntity entity = _shareSummaryRepo.findByCustomerId(customerId);
          return _shareSummaryMapper.toDto(entity);
     }



}
