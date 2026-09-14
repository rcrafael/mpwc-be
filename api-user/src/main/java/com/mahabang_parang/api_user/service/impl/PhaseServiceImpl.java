package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.PhaseDto;
import com.mahabang_parang.api_user.model.mapper.PhaseMapper;
import com.mahabang_parang.api_user.repository.PhaseRepository;
import com.mahabang_parang.api_user.service.PhaseService;

@Service
public class PhaseServiceImpl implements PhaseService {

     private final PhaseRepository phaseRepository;
     private final PhaseMapper mapper;

     public PhaseServiceImpl(PhaseRepository phaseRepository, PhaseMapper mapper) {
          this.phaseRepository = phaseRepository;
          this.mapper = mapper;
     }

     @Override
     public List<PhaseDto> findAll() {
          return mapper.toDtoList(phaseRepository.findAll());
     }

     @Override
     public PhaseDto findById(Integer id) {
          return phaseRepository.findById(id)
                    .map(mapper::toDto)
                    .orElse(null);
     }
}
