package com.mahabang_parang.api_user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mahabang_parang.api_user.model.entity.RoleEntity;
import com.mahabang_parang.api_user.repository.RoleRepository;
import com.mahabang_parang.api_user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

     @Autowired
     private RoleRepository roleRepository;

     @Override
     public List<RoleEntity> findAll() {
          return roleRepository.findAll();
     }

}
