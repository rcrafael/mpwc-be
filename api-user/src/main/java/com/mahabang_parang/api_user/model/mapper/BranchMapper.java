package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.BranchDto;
import com.mahabang_parang.api_user.model.entity.BranchEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BranchMapper {

     BranchDto toDto(BranchEntity entity);
     BranchEntity toEntity(BranchDto dto);

     List<BranchDto> toDtoList(List<BranchEntity> entityList);
     List<BranchEntity> toEntityList(List<BranchDto> dtoList);
}
