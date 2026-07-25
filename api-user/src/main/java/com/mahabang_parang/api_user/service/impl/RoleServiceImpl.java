package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.dto.RoleDto;
import com.mahabang_parang.api_user.model.mapper.RoleMapper;
import com.mahabang_parang.api_user.repository.RoleRepository;
import com.mahabang_parang.api_user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

     private final RoleRepository roleRepository;
     private final RoleMapper mapper;

     RoleServiceImpl(RoleRepository roleRepository, RoleMapper mapper) {
          this.roleRepository = roleRepository;
          this.mapper = mapper;
     }

     @Override
     public List<RoleDto> findAll() {
          return  mapper.toDtoList(roleRepository.findAll());
     }

}
