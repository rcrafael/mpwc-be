package com.mahabang_parang.api_user.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

     UserDto toDto(UserEntity entity);
     UserEntity toEntity(UserDto dto);

     List<UserDto> toList(List<UserEntity> entities);
     List<UserEntity> toEntityList(List<UserDto> dtos);

}
