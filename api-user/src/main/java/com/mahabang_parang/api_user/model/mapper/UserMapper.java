package com.mahabang_parang.api_user.model.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import com.mahabang_parang.api_user.model.dto.UserDto;
import com.mahabang_parang.api_user.model.entity.UserEntity;
import com.mahabang_parang.api_user.model.entity.UserPageAccessEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

     @Mapping(target = "pageAccess", source = "pageAccess", qualifiedByName = "toPageCodeList")
     UserDto toDto(UserEntity entity);

     @Mapping(target = "pageAccess", ignore = true)
     UserEntity toEntity(UserDto dto);

     List<UserDto> toList(List<UserEntity> entities);
     List<UserEntity> toEntityList(List<UserDto> dtos);

     @Named("toPageCodeList")
     default List<String> toPageCodeList(List<UserPageAccessEntity> pageAccessEntities) {
          if (pageAccessEntities == null) {
               return Collections.emptyList();
          }
          return pageAccessEntities.stream()
                    .map(UserPageAccessEntity::getPageCode)
                    .collect(Collectors.toList());
     }

}
