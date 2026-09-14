package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.mapper.BranchMapper;
import com.mahabang_parang.api_user.repository.BranchRepository;
import com.mahabang_parang.api_user.service.BranchService;

@Service
public class BranchServiceImpl implements BranchService {

     private final BranchRepository branchRepository;
     private final BranchMapper mapper;

     public BranchServiceImpl(BranchRepository branchRepository, BranchMapper mapper) {
          this.branchRepository = branchRepository;
          this.mapper = mapper;
     }

     @Override
     public List<BranchDto> findAll() {
          return mapper.toDtoList(branchRepository.findAll());
     }

     @Override
     public BranchDto findById(Integer id) {
          return branchRepository.findById(id)
                    .map(mapper::toDto)
                    .orElse(null);
     }
}
