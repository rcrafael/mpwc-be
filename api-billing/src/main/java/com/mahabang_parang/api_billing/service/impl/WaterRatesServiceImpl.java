package com.mahabang_parang.api_billing.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_billing.model.dto.WaterRatesDto;
import com.mahabang_parang.api_billing.model.entity.WaterRatesCommercialEntity;
import com.mahabang_parang.api_billing.model.entity.WaterRatesResidentialEntity;
import com.mahabang_parang.api_billing.model.mapper.WaterRateMapper;
import com.mahabang_parang.api_billing.repository.WaterRatesCommercialRepository;
import com.mahabang_parang.api_billing.repository.WaterRatesResidentialRepository;
import com.mahabang_parang.api_billing.service.WaterRatesService;



@Transactional
public class WaterRatesServiceImpl implements WaterRatesService {

     @Autowired
     private WaterRatesCommercialRepository commercialRepo;
     @Autowired
     private WaterRatesResidentialRepository residentialRepo;

     @Autowired
     private WaterRateMapper mapper;

     @Override
     public List<WaterRatesDto> getActiveWaterRatesCommercial() {
          List<WaterRatesCommercialEntity> entities = new ArrayList<WaterRatesCommercialEntity>();
          entities = this.commercialRepo.findByActive(true);

          return mapper.toCommercialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getActiveWaterRatesResidentials() {
          List<WaterRatesResidentialEntity> entities = new ArrayList<WaterRatesResidentialEntity>();
          entities = this.residentialRepo.findByActive(true);

          return mapper.toResidentialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getAllWaterRatesCommercial() {
          List<WaterRatesCommercialEntity> entities = new ArrayList<WaterRatesCommercialEntity>();
          entities = this.commercialRepo.findAll(Sort.by(Sort.Direction.ASC,"groupRatingId"));
          return mapper.toCommercialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getAllWaterRatesResidential() {
          List<WaterRatesResidentialEntity> entities = new ArrayList<WaterRatesResidentialEntity>();
          entities = this.residentialRepo.findAll(Sort.by(Sort.Direction.ASC,"groupRatingId"));
          return mapper.toResidentialRateDtoList(entities);

     }

     @Override
     public List<WaterRatesDto> setActiveWaterRatesCommercial(String groupRatingId, Boolean isActive) {
          List<WaterRatesCommercialEntity> entities = this.commercialRepo.findByGroupRatingId(groupRatingId);

          for (WaterRatesCommercialEntity entity : entities) {
               entity.setActive(isActive);
          }

          return mapper.toCommercialRateDtoList(this.commercialRepo.saveAll(entities));
     }

     @Override
     public List<WaterRatesDto> setActiveWaterRatesResidential(String groupRatingId, Boolean isActive) {
          List<WaterRatesResidentialEntity> entities = this.residentialRepo.findByGroupRatingId(groupRatingId);

          for (WaterRatesResidentialEntity entity : entities) {
               entity.setActive(isActive);
          }

          return mapper.toResidentialRateDtoList(this.residentialRepo.saveAll(entities));
     }

     @Override
     public List<WaterRatesDto> addWaterRatesCommercial(List<WaterRatesDto> payload) {
          
          List<WaterRatesCommercialEntity> entities = new ArrayList<WaterRatesCommercialEntity>();
          entities = mapper.toCommercialEntityList(payload);
          List<WaterRatesDto> result = mapper.toCommercialRateDtoList(this.commercialRepo.saveAll(entities));

          return result;
     }

     @Override
     public List<WaterRatesDto> addWaterRatesResidentials(List<WaterRatesDto> payload) {

          List<WaterRatesResidentialEntity> entities = new ArrayList<WaterRatesResidentialEntity>();
          entities = mapper.toResidentialEntityList(payload);
          List<WaterRatesDto> result = mapper.toResidentialRateDtoList(this.residentialRepo.saveAll(entities));

          return result;

     }
     
     @Override
     public List<WaterRatesDto> getWaterRatesCommercialByGroupId(String groupId) {
          List<WaterRatesCommercialEntity> entities = this.commercialRepo.findByGroupRatingId(groupId);
          return mapper.toCommercialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getWaterRatesResidentialByGroupId(String groupId) {
          List<WaterRatesResidentialEntity> entities = this.residentialRepo.findByGroupRatingId(groupId);
          return mapper.toResidentialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> updateWaterRatesCommercial(List<WaterRatesDto> payload) {
          // TODO Auto-generated method stub`
          return null;
     }

     @Override
     public List<WaterRatesDto> updateWaterRatesResidentials(List<WaterRatesDto> payload) {
          // TODO Auto-generated method stub
          return null;
     }
     

}
