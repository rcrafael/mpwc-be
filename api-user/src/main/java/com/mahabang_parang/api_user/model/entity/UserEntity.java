package com.mahabang_parang.api_user.model.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserEntity {
     
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     
     @Column(length = 36, nullable = false)
     private String id;
     private String email;
     private String password;

     @OneToOne
     @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
     private RoleEntity role;


}
