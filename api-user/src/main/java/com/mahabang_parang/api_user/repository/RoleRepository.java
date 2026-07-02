package com.mahabang_parang.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_user.model.entity.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity, String> {

}
