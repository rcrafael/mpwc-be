package com.mahabang_parang.api_settings.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_settings.model.PaginationResponse;
import com.mahabang_parang.api_settings.model.dto.PartDto;
import com.mahabang_parang.api_settings.model.dto.PartsListingDto;
import com.mahabang_parang.api_settings.model.entity.PartEntity;
import com.mahabang_parang.api_settings.model.mapper.PartMapper;
import com.mahabang_parang.api_settings.repository.PartRepository;
import com.mahabang_parang.api_settings.service.PartService;

@Service
public class PartServiceImpl implements PartService {

     private final PartRepository partRepo;
     private final PartMapper mapper;

     public PartServiceImpl(PartRepository partRepo, PartMapper mapper) {
          this.partRepo = partRepo;
          this.mapper = mapper;
     }

     @Override
     public PartDto create(PartDto payload) throws Exception {
          PartEntity part = new PartEntity();
          part.setDescription(payload.getDescription());
          part.setMeasurement(payload.getMeasurement());
          part.setUnitCost(payload.getUnitCost());
          part.setSellingPrice(payload.getSellingPrice());
          part.setUpdatedBy(payload.getUpdatedBy());
          part.setDateUpdated(payload.getDateUpdated());

          PartEntity result = this.partRepo.save(part);
          return this.mapper.toDto(result);
     }

     @Override
     public PaginationResponse<PartDto> getAll(int pageNo, int pageSize) throws Exception {
          Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("Description").ascending()); 

          Page<PartEntity> parts = this.partRepo.findAll(pageable);
          
          return new PaginationResponse<>(
               this.mapper.toDtoList(parts.getContent()),
               parts.getNumber(),
               parts.getSize(),
               parts.getTotalPages(),
               parts.getTotalElements()
          );

     }

     @Override
     public PartDto findById(String id) throws Exception {
          
          PartEntity part = this.partRepo.findById(id).orElseThrow();
          return this.mapper.toDto(part);

     }

     public List<PartsListingDto> listParts() throws Exception {
          List<PartEntity> parts = this.partRepo.findAll(Sort.by(Sort.Direction.ASC, "description"));
          List<PartDto> partsDto = this.mapper.toDtoList(parts);

          List<PartsListingDto> partsListingDto = new ArrayList<PartsListingDto>();

          for (PartDto partDto : partsDto) {
               PartsListingDto part = new PartsListingDto();
               part.setId(partDto.getId());
               part.setDescription(partDto.getDescription() + " - " + partDto.getMeasurement());
               part.setSellingPrice(partDto.getSellingPrice());
               partsListingDto.add(part);
          }

          return partsListingDto;
     }
     
     @Override
     public PartDto update(PartDto payload) throws Exception {
          PartEntity part = this.partRepo.findById(payload.getId()).orElseThrow();
          part.setDescription(payload.getDescription());
          part.setMeasurement(payload.getMeasurement());
          part.setUnitCost(payload.getUnitCost());
          part.setSellingPrice(payload.getSellingPrice());
          part.setUpdatedBy(payload.getUpdatedBy());
          part.setDateUpdated(payload.getDateUpdated());
          PartEntity result = this.partRepo.save(part);
          return this.mapper.toDto(result);
     }
}
