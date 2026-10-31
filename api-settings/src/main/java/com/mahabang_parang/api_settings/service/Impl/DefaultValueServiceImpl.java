package com.mahabang_parang.api_settings.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_settings.model.dto.DefaultValueDto;
import com.mahabang_parang.api_settings.model.entity.DefaultValueEntity;
import com.mahabang_parang.api_settings.model.mapper.DefaultValueMapper;
import com.mahabang_parang.api_settings.repository.DefaultValueRepository;
import com.mahabang_parang.api_settings.service.DefaultValueService;


@Service
@Transactional
public class DefaultValueServiceImpl implements DefaultValueService {

     private final DefaultValueRepository defaultValueRepo;
     private final DefaultValueMapper mapper;

     public DefaultValueServiceImpl(DefaultValueRepository defaultValueRepo, DefaultValueMapper mapper) {
          this.defaultValueRepo = defaultValueRepo;
          this.mapper = mapper;
     }

     @Override
     public DefaultValueDto getDefaultValue() throws Exception {
          DefaultValueEntity entity = defaultValueRepo.findById(1).orElseThrow();
          return mapper.toDto(entity);
     }

     @Override
     public DefaultValueDto updateDefaultValue(DefaultValueDto payload) throws Exception {
          
          DefaultValueEntity entity = defaultValueRepo.findById(1).orElseThrow();
          entity.setMaintenanceFee(payload.getMaintenanceFee());
          entity.setPenaltyPercentage(payload.getPenaltyPercentage());
          entity.setReconnectionFee(payload.getReconnectionFee());
          entity = defaultValueRepo.save(entity);
          return mapper.toDto(entity);

     }

}
