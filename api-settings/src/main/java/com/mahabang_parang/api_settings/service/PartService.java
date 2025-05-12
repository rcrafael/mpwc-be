package com.mahabang_parang.api_settings.service;

import com.mahabang_parang.api_settings.model.ApiResponse;
import com.mahabang_parang.api_settings.model.PaginationResponse;
import com.mahabang_parang.api_settings.model.dto.PartDto;

public interface PartService {

     public PartDto create(PartDto payload) throws Exception;
     public PaginationResponse<PartDto> getAll(int pageNo, int pageSize) throws Exception;
     public PartDto findById(String id) throws Exception;
}
