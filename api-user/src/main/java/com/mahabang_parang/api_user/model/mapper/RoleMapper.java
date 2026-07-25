package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.RoleDto;
import com.mahabang_parang.api_user.model.entity.RoleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

     
     RoleDto toDto(RoleEntity entity);
     RoleEntity toEntity(RoleDto dto);

     List<RoleDto> toDtoList(List<RoleEntity> entities);
     List<RoleEntity> toEntityList(List<RoleDto> dtos);
}
