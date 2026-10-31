package com.mahabang_parang.api_user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mahabang_parang.api_user.model.entity.UserPageAccessEntity;

@Repository
public interface UserPageAccessRepository extends JpaRepository<UserPageAccessEntity, String> {

     List<UserPageAccessEntity> findByUserId(String userId);

     void deleteByUserId(String userId);

}
