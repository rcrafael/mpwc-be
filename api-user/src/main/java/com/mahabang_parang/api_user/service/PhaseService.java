package com.mahabang_parang.api_user.service;

import java.util.List;

import com.mahabang_parang.api_user.model.dto.PhaseDto;

public interface PhaseService {

     List<PhaseDto> findAll();

     PhaseDto findById(Integer id);
}
