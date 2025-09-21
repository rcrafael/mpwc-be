package com.mahabang_parang.api_user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mahabang_parang.api_user.model.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

     UserEntity findByEmail(String email);

}
