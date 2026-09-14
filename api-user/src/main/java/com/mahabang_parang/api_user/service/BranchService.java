package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.dto.BranchDto;

public interface BranchService {

     List<BranchDto> findAll();

     BranchDto findById(Integer id);
}
