package com.mahabang_parang.api_settings.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_settings.model.entity.DefaultValueEntity;

public interface DefaultValueRepository extends JpaRepository<DefaultValueEntity, Integer> {

}
