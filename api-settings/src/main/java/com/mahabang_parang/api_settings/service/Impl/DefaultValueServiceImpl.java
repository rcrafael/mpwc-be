package com.mahabang_parang.api_settings.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
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

     @Autowired
     private DefaultValueRepository defaultValueRepo;
     @Autowired
     private DefaultValueMapper mapper;

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
