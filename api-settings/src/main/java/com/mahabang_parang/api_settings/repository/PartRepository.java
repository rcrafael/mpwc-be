package com.mahabang_parang.api_settings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mahabang_parang.api_settings.model.entity.PartEntity;

public interface PartRepository extends JpaRepository<PartEntity, String> {

}
