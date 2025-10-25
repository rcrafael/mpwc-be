package com.mahabang_parang.api_billing.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mahabang_parang.api_billing.model.dto.WaterRatesDto;
import com.mahabang_parang.api_billing.model.entity.WaterRatesCommercialEntity;
import com.mahabang_parang.api_billing.model.entity.WaterRatesResidentialEntity;
import com.mahabang_parang.api_billing.model.mapper.WaterRateMapper;
import com.mahabang_parang.api_billing.repository.WaterRatesCommercialRepository;
import com.mahabang_parang.api_billing.repository.WaterRatesResidentialRepository;
import com.mahabang_parang.api_billing.service.WaterRatesService;


@Service
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
          Order order = new Order(Sort.Direction.ASC,"hierarchy");
          entities = this.commercialRepo.findByIsActive(true, Sort.by(order));

          return mapper.toCommercialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getActiveWaterRatesResidentials() {
          List<WaterRatesResidentialEntity> entities = new ArrayList<WaterRatesResidentialEntity>();
          Order order = new Order(Sort.Direction.ASC,"hierarchy");
          entities = this.residentialRepo.findByIsActive(true, Sort.by(order));

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

          Order order = new Order(Sort.Direction.ASC,"hierarchy");

          List<WaterRatesCommercialEntity> entities = this.commercialRepo.findByGroupRatingId(groupRatingId, Sort.by(order));

          for (WaterRatesCommercialEntity entity : entities) {
               entity.setIsActive(isActive);
          }

          return mapper.toCommercialRateDtoList(this.commercialRepo.saveAll(entities));
     }

     @Override
     public List<WaterRatesDto> setActiveWaterRatesResidential(String groupRatingId, Boolean isActive) {
          Order order = new Order(Sort.Direction.ASC,"hierarchy");
          List<WaterRatesResidentialEntity> entities = this.residentialRepo.findByGroupRatingId(groupRatingId, Sort.by(order));

          for (WaterRatesResidentialEntity entity : entities) {
               entity.setIsActive(isActive);
          }

          return mapper.toResidentialRateDtoList(this.residentialRepo.saveAll(entities));
     }

     @Override
     public List<WaterRatesDto> addWaterRatesCommercial(List<WaterRatesDto> payload) {
          
          List<WaterRatesCommercialEntity> entities = new ArrayList<WaterRatesCommercialEntity>();
          entities = mapper.toCommercialEntityList(payload);
          
          String groupId = UUID.randomUUID().toString();
          
          for(WaterRatesCommercialEntity ent : entities) {
               ent.setGroupRatingId(groupId);
          }
          
          List<WaterRatesDto> result = mapper.toCommercialRateDtoList(this.commercialRepo.saveAll(entities));

          return result;
     }

     @Override
     public List<WaterRatesDto> addWaterRatesResidentials(List<WaterRatesDto> payload) {

          List<WaterRatesResidentialEntity> entities = new ArrayList<WaterRatesResidentialEntity>();
          entities = mapper.toResidentialEntityList(payload);
          
          String groupId = UUID.randomUUID().toString();
          
          for(WaterRatesResidentialEntity ent : entities) {
               ent.setGroupRatingId(groupId);
          }
          
          List<WaterRatesDto> result = mapper.toResidentialRateDtoList(this.residentialRepo.saveAll(entities));

          return result;

     }
     
     @Override
     public List<WaterRatesDto> getWaterRatesCommercialByGroupId(String groupId) {
          Order order = new Order(Sort.Direction.ASC,"hierarchy");
          List<WaterRatesCommercialEntity> entities = this.commercialRepo.findByGroupRatingId(groupId, Sort.by(order));
          return mapper.toCommercialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> getWaterRatesResidentialByGroupId(String groupId) {
          Order order = new Order(Sort.Direction.ASC,"hierarchy");
          List<WaterRatesResidentialEntity> entities = this.residentialRepo.findByGroupRatingId(groupId, Sort.by(order));
          return mapper.toResidentialRateDtoList(entities);
     }

     @Override
     public List<WaterRatesDto> updateWaterRatesCommercial(List<WaterRatesDto> payload) {
          for(WaterRatesDto dto : payload) {
               WaterRatesCommercialEntity entity = this.commercialRepo.findById(dto.getId()).orElseThrow();
               entity.setRatingValue(dto.getRatingValue());
               this.commercialRepo.save(entity);
          }
          List<WaterRatesDto> result = mapper.toCommercialRateDtoList(this.commercialRepo.findByGroupRatingId(payload.get(0).getGroupRatingId(), Sort.by(Sort.Direction.ASC,"hierarchy")));
          return result;
     }

     @Override
     public List<WaterRatesDto> updateWaterRatesResidentials(List<WaterRatesDto> payload) {
        
          for(WaterRatesDto dto : payload) {
               WaterRatesResidentialEntity entity = this.residentialRepo.findById(dto.getId()).orElseThrow();
               entity.setRatingValue(dto.getRatingValue());
               this.residentialRepo.save(entity);
          }
          List<WaterRatesDto> result = mapper.toResidentialRateDtoList(this.residentialRepo.findByGroupRatingId(payload.get(0).getGroupRatingId(), Sort.by(Sort.Direction.ASC,"hierarchy")));
          return result;

     }
     

}
