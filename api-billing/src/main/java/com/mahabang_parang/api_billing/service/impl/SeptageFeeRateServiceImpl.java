package com.mahabang_parang.api_billing.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_billing.model.dto.SeptageFeeRateDto;
import com.mahabang_parang.api_billing.model.entity.SeptageFeeRateEntity;
import com.mahabang_parang.api_billing.model.mapper.SeptageFeeRateMapper;
import com.mahabang_parang.api_billing.repository.SeptageFeeRateRepository;
import com.mahabang_parang.api_billing.service.SeptageFeeRateService;

@Service
public class SeptageFeeRateServiceImpl implements SeptageFeeRateService {

     @Autowired
     private SeptageFeeRateRepository septageRepo;

     @Autowired
     private SeptageFeeRateMapper mapper;

     @Override
     public SeptageFeeRateDto update(SeptageFeeRateDto payload) throws Exception {
          
          List<SeptageFeeRateEntity> entity = this.septageRepo.findAll();
          SeptageFeeRateEntity payloadToEntity = this.mapper.toEntity(payload);
          SeptageFeeRateEntity result;

          if(entity != null && entity.size() > 0) {
               SeptageFeeRateEntity septageFee = entity.get(0);
               septageFee.setMinimumZeroTo10(payload.getMinimumZeroTo10());
               septageFee.setM3To11(payload.getM3To11());
               septageFee.setM3To12(payload.getM3To12());
               septageFee.setM3To13(payload.getM3To13());
               septageFee.setM3To14(payload.getM3To14());
               septageFee.setM3To15(payload.getM3To15());
               septageFee.setM3To16(payload.getM3To16());
               septageFee.setM3To17(payload.getM3To17());
               septageFee.setM3To18(payload.getM3To18());
               septageFee.setM3To19(payload.getM3To19());
               septageFee.setM3To20(payload.getM3To20());
               septageFee.setM3To21(payload.getM3To21());
               septageFee.setM3To22(payload.getM3To22());
               septageFee.setM3To23(payload.getM3To23());
               septageFee.setM3To24(payload.getM3To24());
               septageFee.setM3To25(payload.getM3To25());
               septageFee.setM3To26(payload.getM3To26());
               septageFee.setM3To27(payload.getM3To27());
               septageFee.setM3To28(payload.getM3To28());
               septageFee.setM3To29(payload.getM3To29());
               septageFee.setM3To30(payload.getM3To30());
               result  = this.septageRepo.save(septageFee);
          } else {
               result = this.septageRepo.save(payloadToEntity);
          }


          return this.mapper.toDto(result);
     }

     @Override
     public SeptageFeeRateDto getRates() throws Exception {
          List<SeptageFeeRateEntity> entity = this.septageRepo.findAll();
          return this.mapper.toDto(entity.get(0));
     }

}
