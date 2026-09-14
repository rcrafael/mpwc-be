package com.mahabang_parang.api_user.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`phase`")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PhaseEntity {

     @Id
     @Column(name = "`id`", nullable = false)
     private Integer id;

     @Column(name = "`Phase`", length = 50)
     private String phase;
}
