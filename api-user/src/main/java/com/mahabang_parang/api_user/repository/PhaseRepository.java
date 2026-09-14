package com.mahabang_parang.api_user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mahabang_parang.api_user.model.entity.PhaseEntity;

@Repository
public interface PhaseRepository extends JpaRepository<PhaseEntity, Integer> {

     @Override
     @Query(value = "SELECT `id`, `Phase` FROM `phase`", nativeQuery = true)
     List<PhaseEntity> findAll();

     @Override
     @Query(value = "SELECT `id`, `Phase` FROM `phase` WHERE `id` = :id", nativeQuery = true)
     Optional<PhaseEntity> findById(@Param("id") Integer id);
}
