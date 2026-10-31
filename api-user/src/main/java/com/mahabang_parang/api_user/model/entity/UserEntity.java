package com.mahabang_parang.api_user.model.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
public class UserEntity implements UserDetails {
     
     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     
     @Column(length = 36, nullable = false)
     private String id;
     private String username;
     private String email;
     private String password;
     private String fname;
     private String lname;
     private String mname;

     @OneToOne
     @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
     private RoleEntity role;

     @jakarta.persistence.OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = jakarta.persistence.FetchType.EAGER)
     private List<UserPageAccessEntity> pageAccess = new java.util.ArrayList<>();

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
         return List.of(new SimpleGrantedAuthority(role.getRole()));
     }

     @Override
     public String getUsername() {
         return this.username;
     }

    
     @Override
     public boolean isAccountNonExpired() {
         return true;
     }
 
     @Override
     public boolean isAccountNonLocked() {
         return true;
     }
 
     @Override
     public boolean isCredentialsNonExpired() {
         return true;
     }

}
