package com.mahabang_parang.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahabang_parang.api_user.model.entity.BranchEntity;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {

}
