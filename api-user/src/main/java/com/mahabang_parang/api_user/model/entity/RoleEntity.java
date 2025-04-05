package com.mahabang_parang.api_user.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_roles")
@NoArgsConstructor
public class RoleEntity {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     private String id;

     private String role;

}
